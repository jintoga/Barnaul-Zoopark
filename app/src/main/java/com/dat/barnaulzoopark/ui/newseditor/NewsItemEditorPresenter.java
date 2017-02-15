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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public void loadSelectedNews(String selectedNewsUid) {
        DatabaseReference newsReference =
            FirebaseDatabase.getInstance().getReference(BZFireBaseApi.news).child(selectedNewsUid);
        newsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News selectedNews = dataSnapshot.getValue(News.class);
                if (selectedNews != null) {
                    if (getView() != null) {
                        getView().bindSelectedNews(selectedNews);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void updateOrCreateNewsItem(@Nullable String newsUID, @NonNull String title,
        @NonNull String description, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments) {
        if (!"".equals(title) && !"".equals(description)) {
            if (getView() != null) {
                getView().showUploadNewsItemProgress();
            }
            createOrUpdateNewsItem(newsUID, title, description, thumbnailUri, attachments);
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

    private void createOrUpdateNewsItem(@Nullable String selectedUid, @NonNull String title,
        @NonNull String description, @Nullable final Uri thumbnailUri,
        @NonNull final List<Attachment> attachments) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        String uid = selectedUid;
        if (uid == null) {
            uid = newsDatabaseReference.push().getKey();
        }
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        News news = new News(uid, title, description, Calendar.getInstance().getTimeInMillis());
        final String finalUid = uid;
        newsItemReference.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (thumbnailUri == null && attachments.isEmpty()) {
                    if (getView() != null) {
                        getView().onUploadSuccess();
                    }
                } else {
                    if (thumbnailUri != null) {
                        uploadImage(false, null, finalUid, thumbnailUri);
                    }
                    if (!attachments.isEmpty()) {
                        uploadAttachments(finalUid, attachments);
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
                Uri uri = Uri.parse(attachment.getUrl());
                uploadImage(true, uid + "-" + String.valueOf(i), uid, uri);
                i++;
            }
        }
    }
}
