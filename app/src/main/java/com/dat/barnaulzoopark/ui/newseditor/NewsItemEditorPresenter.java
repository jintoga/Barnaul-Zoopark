package com.dat.barnaulzoopark.ui.newseditor;

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

    public NewsItemEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
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

    private void createOrUpdateNewsItem(@Nullable String selectedUid, @NonNull final String title,
        @NonNull final String description, @Nullable final Uri thumbnailUri,
        @NonNull final List<Attachment> attachments) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        final String uid;
        if (selectedUid == null) {
            uid = newsDatabaseReference.push().getKey();
        } else {
            uid = selectedUid;
        }
        final DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        newsItemReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                if (news == null) {
                    news =
                        new News(uid, title, description, Calendar.getInstance().getTimeInMillis());
                } else {
                    news.update(title, description);
                }
                NewsItemEditorPresenter.this.updateOrCreateNews(newsItemReference, news,
                    thumbnailUri, attachments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateOrCreateNews(DatabaseReference newsItemReference, @NonNull final News news,
        @Nullable final Uri thumbnailUri, @Nullable final List<Attachment> attachments) {
        newsItemReference.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (thumbnailUri == null && (attachments == null
                    || attachments.size() == 1 && !attachments.get(0).isFilled())) {
                    if (NewsItemEditorPresenter.this.getView() != null) {
                        NewsItemEditorPresenter.this.getView().onUploadSuccess();
                    }
                } else {
                    if (thumbnailUri != null) {
                        NewsItemEditorPresenter.this.uploadImage(false, null, news.getUid(),
                            thumbnailUri);
                    }
                    if (attachments != null && !attachments.isEmpty()) {
                        NewsItemEditorPresenter.this.uploadAttachments(news.getUid(), attachments);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (NewsItemEditorPresenter.this.getView() != null) {
                    NewsItemEditorPresenter.this.getView().onUploadFailure(e.getLocalizedMessage());
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
