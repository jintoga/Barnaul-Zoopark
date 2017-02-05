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

    private void uploadThumbnail(@NonNull final String uid, @NonNull Uri thumbnailUri) {
        if (getView() != null) {
            getView().showUploadThumbnailProgress();
        }
        StorageReference newsStorageReference =
            storage.getReference().child(BZFireBaseApi.news + "/" + uid);
        UploadTask uploadTask = newsStorageReference.putFile(thumbnailUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri thumbnailUploadedUri = taskSnapshot.getDownloadUrl();
                if (thumbnailUploadedUri != null) {
                    setThumbnailToNewsItem(uid, thumbnailUploadedUri);
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

    private void setThumbnailToNewsItem(@NonNull String uid, @NonNull Uri thumbnailUploadedUri) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        newsItemReference.child("thumbnail").setValue(thumbnailUploadedUri.toString());
    }

    private void createNewsItem(@NonNull String title, @NonNull String description,
        @Nullable final Uri thumbnailUri, @NonNull List<Attachment> attachments) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        final String uid = newsDatabaseReference.push().getKey();
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        News news = new News(uid, title, description, Calendar.getInstance().getTimeInMillis());
        newsItemReference.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (thumbnailUri != null) {
                    uploadThumbnail(uid, thumbnailUri);
                } else {
                    if (getView() != null) {
                        getView().onUploadSuccess();
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

    private void updateNewsItem(@NonNull String newsUID, @NonNull String title,
        @NonNull String description, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments) {

    }
}
