package com.dat.barnaulzoopark.ui.videoalbumeditor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import java.util.Date;
import java.util.List;
import rx.Observable;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbumEditorPresenter extends MvpBasePresenter<VideoAlbumEditorContract.View>
    implements VideoAlbumEditorContract.UserActionListener {
    private FirebaseDatabase database;

    VideoAlbumEditorPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void createVideoAlbum(@NonNull String name, @Nullable Date dateCreated,
        @NonNull List<Attachment> filledAttachments) {
        if (!"".equals(name) && dateCreated != null) {
            create(name, dateCreated, filledAttachments);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void create(@NonNull String name, @NonNull Date dateCreated,
        @NonNull List<Attachment> attachments) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.video_album);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference itemReference = databaseReference.child(uid);
        VideoAlbum videoAlbum = new VideoAlbum(uid, name, dateCreated.getTime());
        itemReference.setValue(videoAlbum);
        if (getView() != null) {
            getView().showCreatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(itemReference, VideoAlbum.class)
            .flatMap(videoAlbum1 -> uploadAttachments(videoAlbum1, attachments))
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
    private Observable<VideoAlbum> uploadAttachments(@NonNull VideoAlbum videoAlbum,
        @NonNull List<Attachment> attachments) {
        return Observable.from(attachments)
            .filter(
                (attachment1) -> attachment1.isFilled() && attachment1.getAttachmentUid() == null)
            .flatMap(attachment -> uploadAttachment(videoAlbum, attachment.getUrl()));
    }

    @NonNull
    private Observable<VideoAlbum> uploadAttachment(@NonNull VideoAlbum videoAlbum,
        @NonNull String videoId) {
        final String attachmentUid = database.getReference().push().getKey();
        return setAttachmentToVideoAlbum(videoAlbum, attachmentUid, videoId);
    }

    @NonNull
    private Observable<VideoAlbum> setAttachmentToVideoAlbum(@NonNull VideoAlbum videoAlbum,
        @NonNull String attachmentUid, @NonNull String videoId) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.video_album);
        DatabaseReference itemReference = databaseReference.child(videoAlbum.getUid());
        DatabaseReference itemPhotoReference = itemReference.child("videos");
        itemPhotoReference.child(attachmentUid).setValue(videoId);
        return Observable.just(videoAlbum);
    }

    @Override
    public void editVideoAlbum(VideoAlbum selectedVideoAlbum, @NonNull String name,
        @Nullable Date dateCreated, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        if (!"".equals(name) && dateCreated != null) {
            edit(selectedVideoAlbum, name, dateCreated, attachmentsToAdd, attachmentsToDelete);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void edit(@NonNull VideoAlbum selectedVideoAlbum, @NonNull String name,
        @NonNull Date dateCreated, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.video_album);
        final DatabaseReference itemReference =
            databaseReference.child(selectedVideoAlbum.getUid());
        selectedVideoAlbum.update(name, dateCreated.getTime());
        itemReference.setValue(selectedVideoAlbum);
        if (getView() != null) {
            getView().showEditingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(itemReference, VideoAlbum.class)
            .flatMap(
                videoAlbum -> updateAttachments(selectedVideoAlbum, itemReference, attachmentsToAdd,
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
    private Observable<VideoAlbum> updateAttachments(@NonNull VideoAlbum videoAlbum,
        @NonNull DatabaseReference reference, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        return Observable.concat(deleteAttachments(videoAlbum, reference, attachmentsToDelete),
            uploadAttachments(videoAlbum, attachmentsToAdd));
    }

    @NonNull
    private Observable<VideoAlbum> deleteAttachments(@NonNull VideoAlbum videoAlbum,
        @NonNull DatabaseReference itemReference, @NonNull List<Attachment> attachmentsToDelete) {
        return Observable.from(attachmentsToDelete)
            .filter(attachment -> attachment.getAttachmentUid() != null)
            .flatMap(attachment -> {
                videoAlbum.getVideos().remove(attachment.getAttachmentUid());
                itemReference.setValue(videoAlbum);
                return Observable.just(videoAlbum);
            });
    }

    @Override
    public void loadSelectedVideoAlbum(@NonNull String selectedVideoAlbumUid) {
        DatabaseReference databaseReference =
            database.getReference(BZFireBaseApi.video_album).child(selectedVideoAlbumUid);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(databaseReference, VideoAlbum.class)
            .subscribe(videoAlbum -> {
                if (getView() != null) {
                    getView().bindSelectedVideoAlbum(videoAlbum);
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
