package com.dat.barnaulzoopark.ui.newseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import java.util.Calendar;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

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
    public void createNewsItem(@NonNull String title, @NonNull String description,
        @Nullable Uri thumbnailUri, @NonNull List<Attachment> attachments) {
        if (!"".equals(title) && !"".equals(description)) {
            if (getView() != null) {
                getView().creatingNewsItemProgress();
            }
            createNews(title, description, thumbnailUri, attachments);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private Observable<Uri> uploadImage(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference newsStorageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(newsStorageReference, uri)
            .flatMap(new Func1<UploadTask.TaskSnapshot, Observable<Uri>>() {
                @Override
                public Observable<Uri> call(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uploadedUri = taskSnapshot.getDownloadUrl();
                    return Observable.just(uploadedUri);
                }
            });
    }

    private Observable<News> addAttachmentToNewsItem(@NonNull String uid, @NonNull Uri uploadedUri,
        String attachmentUid) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        DatabaseReference newsItemPhotoReference = newsItemReference.child("photos");
        newsItemPhotoReference.child(attachmentUid).setValue(uploadedUri.toString());
        return RxFirebaseDatabase.observeSingleValueEvent(newsDatabaseReference, News.class);
    }

    private Observable<News> setThumbnailToNewsItem(@NonNull String uid,
        @NonNull Uri thumbnailUploadedUri) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        newsItemReference.child("thumbnail").setValue(thumbnailUploadedUri.toString());
        return RxFirebaseDatabase.observeSingleValueEvent(newsDatabaseReference, News.class);
    }

    private void createNews(@NonNull final String title, @NonNull final String description,
        @Nullable final Uri thumbnailUri, @NonNull final List<Attachment> attachments) {
        DatabaseReference newsDatabaseReference = database.getReference().child(BZFireBaseApi.news);
        final String uid = newsDatabaseReference.push().getKey();
        final DatabaseReference newsItemReference = newsDatabaseReference.child(uid);
        News news = new News(uid, title, description, Calendar.getInstance().getTimeInMillis());
        newsItemReference.setValue(news);
        RxFirebaseDatabase.observeSingleValueEvent(newsItemReference, News.class)
            .subscribe(new Action1<News>() {
                @Override
                public void call(News news) {
                    if (getView() != null) {
                        getView().onCreatingNewsItemSuccess();
                    }
                    if (news != null) {
                        uploadImages(news, thumbnailUri, attachments);
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    if (getView() != null) {
                        getView().onCreatingNewsItemFailure(throwable.getLocalizedMessage());
                    }
                }
            });
    }

    private void uploadImages(@NonNull News news, @Nullable Uri thumbnailUri,
        @NonNull List<Attachment> attachments) {
        if (thumbnailUri == null && (attachments.size() == 1 && !attachments.get(0).isFilled())) {
            if (NewsItemEditorPresenter.this.getView() != null) {
                NewsItemEditorPresenter.this.getView().onAllComplete();
            }
        } else {
            if (thumbnailUri == null) {
                uploadAttachments(news, attachments).subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {
                        if (getView() != null) {
                            getView().onAllComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onUploadFailure(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onNext(News news) {

                    }
                });
            } else {
                Observable.concat(uploadThumbnail(news, thumbnailUri),
                    uploadAttachments(news, attachments)).subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {
                        if (getView() != null) {
                            getView().onAllComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onUploadFailure(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onNext(News news) {

                    }
                });
            }
        }
    }

    private Observable<News> uploadThumbnail(@NonNull final News news, @NonNull Uri uri) {
        if (getView() != null) {
            getView().uploadingThumbnailProgress();
        }
        String filePath = BZFireBaseApi.news + "/" + news.getUid() + "/" + "thumbnail";
        return uploadImage(filePath, uri).flatMap(new Func1<Uri, Observable<News>>() {
            @Override
            public Observable<News> call(Uri uploadedUri) {
                return setThumbnailToNewsItem(news.getUid(), uploadedUri);
            }
        });
    }

    private Observable<News> uploadAttachments(@NonNull final News news,
        @NonNull final List<Attachment> attachments) {
        if (getView() != null) {
            getView().uploadingAttachments();
        }
        final int[] i = { 0 };
        return Observable.from(attachments).filter(new Func1<Attachment, Boolean>() {
            @Override
            public Boolean call(Attachment attachment) {
                return attachment.isFilled();
            }
        }).flatMap(new Func1<Attachment, Observable<News>>() {
            @Override
            public Observable<News> call(Attachment attachment) {
                return uploadAttachmentItem(news, attachment, i[0]++);
            }
        });
    }

    private Observable<News> uploadAttachmentItem(final News news, Attachment attachment, int i) {
        Uri uri = Uri.parse(attachment.getUrl());
        final String attachmentUid = news.getUid() + "-" + String.valueOf(i);
        final String attachmentPath =
            BZFireBaseApi.news + "/" + news.getUid() + "/photos/" + attachmentUid;
        return uploadImage(attachmentPath, uri).flatMap(new Func1<Uri, Observable<News>>() {
            @Override
            public Observable<News> call(Uri uploadedUri) {
                return addAttachmentToNewsItem(news.getUid(), uploadedUri, attachmentUid);
            }
        });
    }
}
