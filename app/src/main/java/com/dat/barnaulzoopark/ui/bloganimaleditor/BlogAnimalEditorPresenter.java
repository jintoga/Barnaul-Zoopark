package com.dat.barnaulzoopark.ui.bloganimaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import rx.Observable;

/**
 * Created by DAT on 5/13/2017.
 */

class BlogAnimalEditorPresenter extends MvpBasePresenter<BlogAnimalEditorContract.View>
    implements BlogAnimalEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    BlogAnimalEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createAnimal(@NonNull String title, @NonNull String description,
        @NonNull String animalUid, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments, @NonNull String videoUrl) {
        if (!"".equals(title) && !"".equals(description) && !"".equals(animalUid)) {
            create(title, description, animalUid, thumbnailUri, attachments, videoUrl);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void create(@NonNull String title, @NonNull String description,
        @NonNull String animalUid, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments, @NonNull String videoUrl) {
        DatabaseReference blogDatabaseReference =
            database.getReference().child(BZFireBaseApi.blog_animal);
        final String uid = blogDatabaseReference.push().getKey();
        final DatabaseReference blogItemReference = blogDatabaseReference.child(uid);
        BlogAnimal blogAnimal = new BlogAnimal(uid, animalUid, title, description,
            Calendar.getInstance().getTimeInMillis());
        if (!"".equals(videoUrl)) {
            blogAnimal.setVideo(videoUrl);
        }
        blogItemReference.setValue(blogAnimal);
        if (getView() != null) {
            getView().creatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(blogItemReference, BlogAnimal.class)
            .flatMap(blogAnimal1 -> uploadIThumbnail(blogAnimal1, thumbnailUri))
            .flatMap(blogAnimal1 -> uploadAttachments(blogAnimal1, attachments))
            .subscribe(blogAnimal1 -> {
            }, throwable -> {
                if (getView() != null) {
                    getView().onCreatingFailure(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onCreatingSuccess();
                }
            });
    }

    @NonNull
    private Observable<BlogAnimal> uploadAttachments(@NonNull BlogAnimal blogAnimal,
        @NonNull List<Attachment> attachments) {
        return Observable.from(attachments)
            .filter(
                (attachment1) -> attachment1.isFilled() && attachment1.getAttachmentUid() == null)
            .flatMap(attachment -> {
                Uri uri = Uri.parse(attachment.getUrl());
                return uploadAttachmentImage(blogAnimal, uri);
            });
    }

    @NonNull
    private Observable<BlogAnimal> uploadAttachmentImage(@NonNull BlogAnimal blogAnimal,
        @NonNull Uri uri) {
        final String attachmentUid = database.getReference().push().getKey();
        final String attachmentPath =
            BZFireBaseApi.blog_animal + "/" + blogAnimal.getUid() + "/photos/" + attachmentUid;
        return uploadFile(attachmentPath, uri).flatMap(
            uploadedUri -> setAttachmentToBlog(blogAnimal, attachmentUid, uploadedUri));
    }

    @NonNull
    private Observable<BlogAnimal> setAttachmentToBlog(@NonNull BlogAnimal blogAnimal,
        @NonNull String attachmentUid, Uri uploadedUri) {
        DatabaseReference blogDatabaseReference =
            database.getReference().child(BZFireBaseApi.blog_animal);
        DatabaseReference blogItemReference = blogDatabaseReference.child(blogAnimal.getUid());
        DatabaseReference animalItemPhotoReference = blogItemReference.child("photos");
        animalItemPhotoReference.child(attachmentUid).setValue(uploadedUri.toString());
        return Observable.just(blogAnimal);
    }

    @NonNull
    private Observable<BlogAnimal> uploadIThumbnail(@NonNull BlogAnimal blogAnimal,
        @Nullable Uri thumbnailUri) {
        if (thumbnailUri == null) {
            return Observable.just(blogAnimal);
        } else {
            return getObservableUploadIcon(blogAnimal, thumbnailUri);
        }
    }

    @NonNull
    private Observable<BlogAnimal> getObservableUploadIcon(@NonNull BlogAnimal blogAnimal,
        @NonNull Uri uri) {
        String filePath = BZFireBaseApi.blog_animal + "/" + blogAnimal.getUid() + "/" + "thumbnail";
        return uploadFile(filePath, uri).flatMap(
            uploadedUri -> setThumbnailToBlog(blogAnimal, uploadedUri));
    }

    @NonNull
    private Observable<BlogAnimal> setThumbnailToBlog(@NonNull BlogAnimal blogAnimal,
        @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.blog_animal);
        DatabaseReference blogAnimalReference = databaseReference.child(blogAnimal.getUid());
        blogAnimalReference.child("thumbnail").setValue(uploadedUri.toString());
        return Observable.just(blogAnimal);
    }

    @NonNull
    private Observable<Uri> uploadFile(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference newsStorageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(newsStorageReference, uri)
            .flatMap(taskSnapshot -> Observable.just(taskSnapshot.getDownloadUrl()));
    }

    @Override
    public void loadAnimals() {
        database.getReference(BZFireBaseApi.animal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Animal> animals = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Animal animal = snapshot.getValue(Animal.class);
                    animals.add(animal);
                }
                if (getView() != null) {
                    getView().bindAnimals(animals);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (getView() != null) {
                    getView().onLoadAnimalsError(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void loadSelectedBlog(@NonNull String selectedBlogUid) {
        DatabaseReference blogReference =
            database.getReference(BZFireBaseApi.blog_animal).child(selectedBlogUid);
        if (getView() != null) {
            getView().onLoadBlogProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(blogReference, BlogAnimal.class)
            .subscribe(blogAnimal -> {
                if (getView() != null) {
                    getView().bindSelectedAnimal(blogAnimal);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onLoadBlogError(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onLoadBlogSuccess();
                }
            });
    }
}
