package com.dat.barnaulzoopark.ui.newseditor;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorPresenter extends MvpBasePresenter<NewsItemEditorContract.View>
    implements NewsItemEditorContract.UserActionListener {
    private static final String TAG = NewsItemEditorPresenter.class.getName();
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    public NewsItemEditorPresenter(@NonNull Context context, FirebaseDatabase database,
        FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void updateOrCreateNewsItem(@Nullable String newsUID, @NonNull String title,
        @NonNull String description, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments) {
        if (!"".equals(title) && !"".equals(description)) {
            if (getView() != null) {
                getView().showUploadNewsItemProgress();
            }
            if (newsUID == null) {
                createNewsItem(title, description, thumbnailUri, attachments);
            } else {
                updateNewsItem(newsUID, title, description, thumbnailUri, attachments);
            }
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void uploadImage(final boolean isAttachment, @Nullable final String attachmentUid,
        @NonNull final String uid, @NonNull Uri uri) {
        if (getView() != null) {
            getView().showUploadThumbnailProgress();
        }
        StorageReference newsStorageReference;
        if (!isAttachment) {
            newsStorageReference =
                storage.getReference().child(BZFireBaseApi.news + "/" + uid + "/" + "thumbnail");
        } else {
            newsStorageReference = storage.getReference()
                .child(BZFireBaseApi.news + "/" + uid + "/photos/" + attachmentUid);
        }
        UploadTask uploadTask = newsStorageReference.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uploadedUri = taskSnapshot.getDownloadUrl();
                if (!isAttachment && uploadedUri != null) {
                    setThumbnailToNewsItem(uid, uploadedUri);
                } else if (uploadedUri != null) {
                    addAttachmentToNewsItem(uid, uploadedUri, attachmentUid);
                }
                if (getView() != null) {
                    getView().onUploadSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (getView() != null) {
                    getView().onUploadFailure(e.getLocalizedMessage());
                }
            }
        });
    }

    private void addAttachmentToNewsItem(@NonNull String uid, @NonNull Uri uploadedUri,
        String attachmentUid) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        DatabaseReference newsItemPhotoReference = newsItemReference.child("photos");
        newsItemPhotoReference.child(attachmentUid).setValue(uploadedUri.toString());
    }

    private void setThumbnailToNewsItem(@NonNull String uid, @NonNull Uri thumbnailUploadedUri) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        newsItemReference.child("thumbnail").setValue(thumbnailUploadedUri.toString());
    }

    private void createNewsItem(@NonNull String title, @NonNull String description,
        @Nullable final Uri thumbnailUri, @NonNull final List<Attachment> attachments) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        final String uid = newsDatabaseReference.push().getKey();
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        News news = new News(uid, title, description, Calendar.getInstance().getTimeInMillis());
        newsItemReference.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (thumbnailUri == null && attachments.isEmpty()) {
                    if (getView() != null) {
                        getView().onUploadSuccess();
                    }
                } else {
                    if (thumbnailUri != null) {
                        uploadImage(false, null, uid, thumbnailUri);
                    }
                    if (!attachments.isEmpty()) {
                        uploadAttachments(uid, attachments);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (getView() != null) {
                    getView().onUploadFailure(e.getLocalizedMessage());
                }
            }
        });
    }

    private void uploadAttachments(@NonNull String uid, @NonNull List<Attachment> attachments) {
        int i = 0;
        for (Attachment attachment : attachments) {
            if (attachment.isFilled()) {
                uploadImage(true, uid + String.valueOf(i), uid, attachment.getUri());
                i++;
            }
        }
    }

    private void updateNewsItem(@NonNull String newsUID, @NonNull String title,
        @NonNull String description, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments) {

    }
}
