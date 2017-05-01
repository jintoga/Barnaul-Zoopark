package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorPresenter extends MvpBasePresenter<CategoryEditorContract.View>
    implements CategoryEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    public CategoryEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createCategory(@NonNull String name, @NonNull String description,
        @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(description)) {
            if (getView() != null) {
                getView().showCreatingProgress();
            }
            create(name, description, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void create(@NonNull String name, @NonNull String description,
        @Nullable final Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_categories);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference categoryItemReference = databaseReference.child(uid);
        Category category = new Category(uid, name, description);
        categoryItemReference.setValue(category);
        RxFirebaseDatabase.observeSingleValueEvent(categoryItemReference, Category.class)
            .subscribe(new Action1<Category>() {
                @Override
                public void call(Category category) {
                    if (getView() != null) {
                        getView().onCreatingCategorySuccess();
                    }
                    if (category != null) {
                        uploadIcon(category, iconUri);
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    if (getView() != null) {
                        getView().onCreatingCategoryFailure(throwable.getLocalizedMessage());
                    }
                }
            });
    }

    private void uploadIcon(@NonNull Category category, @Nullable Uri iconUri) {
        if (iconUri == null) {
            if (getView() != null) {
                getView().onCreatingComplete();
            }
        } else {
            getObservableUploadIcon(category, iconUri).subscribe(new Observer<Category>() {
                @Override
                public void onCompleted() {
                    if (getView() != null) {
                        getView().onCreatingComplete();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().onUploadFailure(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onNext(Category category) {

                }
            });
        }
    }

    private Observable<Category> getObservableUploadIcon(@NonNull final Category category,
        @NonNull Uri uri) {
        if (getView() != null) {
            getView().uploadingIconProgress();
        }
        String filePath = BZFireBaseApi.animal_categories + "/" + category.getUid() + "/" + "icon";
        return uploadImage(filePath, uri).flatMap(new Func1<Uri, Observable<Category>>() {
            @Override
            public Observable<Category> call(Uri uploadedUri) {
                return setIconToCategory(category.getUid(), uploadedUri);
            }
        });
    }

    private Observable<Category> setIconToCategory(@NonNull String uid, @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_categories);
        DatabaseReference categoryReference = databaseReference.child(uid);
        categoryReference.child("icon").setValue(uploadedUri.toString());
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference, Category.class);
    }

    private Observable<Uri> uploadImage(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference categoryStorageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(categoryStorageReference, uri)
            .flatMap(new Func1<UploadTask.TaskSnapshot, Observable<Uri>>() {
                @Override
                public Observable<Uri> call(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uploadedUri = taskSnapshot.getDownloadUrl();
                    return Observable.just(uploadedUri);
                }
            });
    }
}
