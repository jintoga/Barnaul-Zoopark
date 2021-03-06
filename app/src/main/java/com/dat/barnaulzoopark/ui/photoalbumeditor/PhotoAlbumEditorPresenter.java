package com.dat.barnaulzoopark.ui.photoalbumeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import java.util.Date;
import java.util.List;
import rx.Observable;

/**
 * Created by DAT on 5/21/2017.
 */

class PhotoAlbumEditorPresenter extends MvpBasePresenter<PhotoAlbumEditorContract.View>
    implements PhotoAlbumEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    PhotoAlbumEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createPhotoAlbum(@NonNull String name, @Nullable Date dateCreated,
        @NonNull List<Attachment> filledAttachments) {
        if (!"".equals(name) && dateCreated != null) {
            create(name, dateCreated, filledAttachments);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    @Override
    public void editPhotoAlbum(@NonNull PhotoAlbum selectedPhotoAlbum, @NonNull String name,
        @Nullable Date dateCreated, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        if (!"".equals(name) && dateCreated != null) {
            edit(selectedPhotoAlbum, name, dateCreated, attachmentsToAdd, attachmentsToDelete);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void edit(@NonNull PhotoAlbum selectedPhotoAlbum, @NonNull String name,
        @NonNull Date dateCreated, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.photo_album);
        final DatabaseReference itemReference =
            databaseReference.child(selectedPhotoAlbum.getUid());
        selectedPhotoAlbum.update(name, dateCreated.getTime());
        itemReference.setValue(selectedPhotoAlbum);
        if (getView() != null) {
            getView().showEditingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(itemReference, PhotoAlbum.class)
            .flatMap(
                photoAlbum -> updateAttachments(selectedPhotoAlbum, itemReference, attachmentsToAdd,
                    attachmentsToDelete))
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onEditSuccess();
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onEditError(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }

    @NonNull
    private Observable<PhotoAlbum> updateAttachments(@NonNull PhotoAlbum photoAlbum,
        @NonNull DatabaseReference reference, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        return Observable.concat(deleteAttachments(photoAlbum, reference, attachmentsToDelete),
            uploadAttachments(photoAlbum, attachmentsToAdd));
    }

    @NonNull
    private Observable<PhotoAlbum> deleteAttachments(@NonNull PhotoAlbum photoAlbum,
        @NonNull DatabaseReference itemReference, @NonNull List<Attachment> attachmentsToDelete) {
        return Observable.from(attachmentsToDelete)
            .filter(attachment -> attachment.getAttachmentUid() != null)
            .flatMap(attachment -> {
                final String attachmentPath = BZFireBaseApi.photo_album
                    + "/"
                    + photoAlbum.getUid()
                    + "/photos/"
                    + attachment.getAttachmentUid();
                return deleteAttachment(attachmentPath).flatMap(aVoid -> {
                    photoAlbum.getPhotos().remove(attachment.getAttachmentUid());
                    itemReference.setValue(photoAlbum);
                    return Observable.just(photoAlbum);
                });
            });
    }

    @NonNull
    private Observable<Void> deleteAttachment(@NonNull String filePath) {
        StorageReference storageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(storageReference);
    }

    private void create(@NonNull String name, @NonNull Date dateCreated,
        @NonNull List<Attachment> attachments) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.photo_album);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference photoAlbumItemReference = databaseReference.child(uid);
        PhotoAlbum photoAlbum = new PhotoAlbum(uid, name, dateCreated.getTime());
        photoAlbumItemReference.setValue(photoAlbum);
        if (getView() != null) {
            getView().showCreatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(photoAlbumItemReference, PhotoAlbum.class)
            .flatMap(photoAlbum1 -> uploadAttachments(photoAlbum1, attachments))
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onCreatingSuccess();
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onCreatingFailure(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }

    @NonNull
    private Observable<PhotoAlbum> uploadAttachments(@NonNull PhotoAlbum photoAlbum,
        @NonNull List<Attachment> attachments) {
        return Observable.from(attachments)
            .filter(
                (attachment1) -> attachment1.isFilled() && attachment1.getAttachmentUid() == null)
            .flatMap(attachment -> {
                Uri uri = Uri.parse(attachment.getUrl());
                return uploadAttachmentImage(photoAlbum, uri);
            });
    }

    @NonNull
    private Observable<PhotoAlbum> uploadAttachmentImage(@NonNull PhotoAlbum photoAlbum,
        @NonNull Uri uri) {
        final String attachmentUid = database.getReference().push().getKey();
        final String attachmentPath =
            BZFireBaseApi.photo_album + "/" + photoAlbum.getUid() + "/photos/" + attachmentUid;
        return uploadFile(attachmentPath, uri).flatMap(
            uploadedUri -> setAttachmentToPhotoAlbum(photoAlbum, attachmentUid, uploadedUri));
    }

    @NonNull
    private Observable<PhotoAlbum> setAttachmentToPhotoAlbum(@NonNull PhotoAlbum photoAlbum,
        @NonNull String attachmentUid, Uri uploadedUri) {
        DatabaseReference animalDatabaseReference =
            database.getReference().child(BZFireBaseApi.photo_album);
        DatabaseReference animalItemReference = animalDatabaseReference.child(photoAlbum.getUid());
        DatabaseReference animalItemPhotoReference = animalItemReference.child("photos");
        animalItemPhotoReference.child(attachmentUid).setValue(uploadedUri.toString());
        return Observable.just(photoAlbum);
    }

    @NonNull
    private Observable<Uri> uploadFile(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference storageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(storageReference, uri)
            .flatMap(taskSnapshot -> Observable.just(taskSnapshot.getDownloadUrl()));
    }

    @Override
    public void loadSelectedPhotoAlbum(@NonNull String selectedPhotoAlbumUid) {
        DatabaseReference databaseReference =
            database.getReference(BZFireBaseApi.photo_album).child(selectedPhotoAlbumUid);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(databaseReference, PhotoAlbum.class)
            .subscribe(photoAlbum -> {
                if (getView() != null) {
                    getView().bindSelectedPhotoAlbum(photoAlbum);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onLoadError(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onLoadSuccess();
                }
            });
    }
}
