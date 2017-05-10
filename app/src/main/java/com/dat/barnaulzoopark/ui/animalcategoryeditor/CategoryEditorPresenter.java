package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.utils.UriUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import rx.Observable;

/**
 * Created by DAT on 4/23/2017.
 */

class CategoryEditorPresenter extends MvpBasePresenter<CategoryEditorContract.View>
    implements CategoryEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    CategoryEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createCategory(@NonNull String name, @NonNull String description,
        @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(description)) {
            create(name, description, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    @Override
    public void editCategory(@NonNull Category selectedCategory, @NonNull String name,
        @NonNull String description, @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(description)) {
            edit(selectedCategory, name, description, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void edit(@NonNull Category selectedCategory, @NonNull String name,
        @NonNull String description, @Nullable final Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_categories);
        final DatabaseReference categoryItemReference =
            databaseReference.child(selectedCategory.getUid());
        selectedCategory.update(name, description);
        categoryItemReference.setValue(selectedCategory);
        if (getView() != null) {
            getView().showEditingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(categoryItemReference, Category.class)
            .flatMap(category -> updateIcon(selectedCategory, iconUri, categoryItemReference))
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
    private Observable<Category> updateIcon(@NonNull Category selectedCategory,
        @Nullable Uri iconUri, @NonNull DatabaseReference categoryItemReference) {
        String filePath =
            BZFireBaseApi.animal_categories + "/" + selectedCategory.getUid() + "/" + "icon";
        if (iconUri == null && selectedCategory.getIcon() != null) {
            //delete
            return deleteIcon(selectedCategory, categoryItemReference, filePath);
        } else if (iconUri != null && UriUtil.isLocalFile(iconUri)) {
            //update
            return uploadIcon(selectedCategory, iconUri);
        } else {
            return Observable.just(selectedCategory);
        }
    }

    @NonNull
    private Observable<Category> deleteIcon(Category selectedCategory,
        DatabaseReference categoryItemReference, String filePath) {
        StorageReference newsStorageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(newsStorageReference).flatMap(aVoid -> {
            //set icon Value in Selected category to null
            selectedCategory.setIcon(null);
            categoryItemReference.setValue(selectedCategory);
            return Observable.just(selectedCategory);
        });
    }

    @Override
    public void loadSelectedCategory(@NonNull String selectedCategoryUid) {
        DatabaseReference categoryReference =
            database.getReference(BZFireBaseApi.animal_categories).child(selectedCategoryUid);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(categoryReference, Category.class)
            .subscribe(selectedCategory -> {
                if (getView() != null) {
                    getView().bindSelectedCategory(selectedCategory);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onLoadCategoryError(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onLoadCategorySuccess();
                }
            });
    }

    private void create(@NonNull String name, @NonNull String description,
        @Nullable final Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_categories);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference categoryItemReference = databaseReference.child(uid);
        Category category = new Category(uid, name, description);
        categoryItemReference.setValue(category);
        if (getView() != null) {
            getView().showCreatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(categoryItemReference, Category.class)
            .flatMap(category1 -> uploadIcon(category1, iconUri))
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onCreatingSuccess();
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onCreatingCategoryFailure(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }

    @NonNull
    private Observable<Category> uploadIcon(@NonNull Category category, @Nullable Uri iconUri) {
        if (iconUri == null) {
            return Observable.just(category);
        } else {
            return getObservableUploadIcon(category, iconUri);
        }
    }

    @NonNull
    private Observable<Category> getObservableUploadIcon(@NonNull final Category category,
        @NonNull Uri uri) {
        String filePath = BZFireBaseApi.animal_categories + "/" + category.getUid() + "/" + "icon";
        return uploadImage(filePath, uri).flatMap(
            uploadedUri -> setIconToCategory(category, uploadedUri));
    }

    @NonNull
    private Observable<Uri> uploadImage(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference categoryStorageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(categoryStorageReference, uri).flatMap(taskSnapshot -> {
            Uri uploadedUri = taskSnapshot.getDownloadUrl();
            return Observable.just(uploadedUri);
        });
    }

    @NonNull
    private Observable<Category> setIconToCategory(@NonNull Category category,
        @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_categories);
        DatabaseReference categoryReference = databaseReference.child(category.getUid());
        categoryReference.child("icon").setValue(uploadedUri.toString());
        return Observable.just(category);
    }
}
