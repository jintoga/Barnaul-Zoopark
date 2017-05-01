package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.AbstractData;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
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
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementPresenter extends MvpBasePresenter<DataManagementContract.View>
    implements DataManagementContract.UserActionListener {

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    DataManagementPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public DatabaseReference getDataReference(String referenceName) {
        return database.getReference(referenceName);
    }

    @Override
    public void removeItem(AbstractData data) {
        if (data instanceof Animal) {
            //databaseReference = database.getReference(BZFireBaseApi.animal);
        } else if (data instanceof Species) {
            //databaseReference = database.getReference(BZFireBaseApi.animal_species);
        } else if (data instanceof Category) {
            removeCategory(data);
        } else {
            if (getView() != null) {
                getView().onRemoveError("DatabaseReference NULL");
            }
        }
    }

    private void removeCategory(@NonNull final AbstractData data) {
        final DatabaseReference databaseReference =
            database.getReference(BZFireBaseApi.animal_categories);
        final DatabaseReference categoryReference = databaseReference.child(data.getId());
        if (getView() != null) {
            getView().showRemoveProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(categoryReference, Category.class)
            .flatMap(new Func1<Category, Observable<Category>>() {
                @Override
                public Observable<Category> call(Category category) {
                    return getDeleteCategoryIconObservable(category);
                }
            })
            .doOnNext(new Action1<Category>() {
                @Override
                public void call(Category category) {
                    deleteSpeciesCategoryUid(category);
                }
            })
            .subscribe(new Observer<Category>() {
                @Override
                public void onCompleted() {
                    if (getView() != null) {
                        getView().showRemoveSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().onRemoveError(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onNext(Category category) {
                    databaseReference.child(category.getUid()).removeValue();
                }
            });
    }

    private void deleteSpeciesCategoryUid(@NonNull final Category category) {
        final DatabaseReference databaseReference =
            database.getReference(BZFireBaseApi.animal_species);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Species species = snapshot.getValue(Species.class);
                    if (species.getCategoryUid() != null && species.getCategoryUid()
                        .equals(category.getUid())) {
                        species.setCategoryUid(null);
                        databaseReference.child(species.getUid()).setValue(species);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private Observable<Category> getDeleteCategoryIconObservable(@NonNull final Category category) {
        String filePath = BZFireBaseApi.animal_categories + "/" + category.getUid() + "/" + "icon";
        if (category.getIcon() == null || category.getIcon().isEmpty()) {
            return Observable.just(category);
        }
        return deleteNewsItemFile(filePath).flatMap(new Func1<Void, Observable<Category>>() {
            @Override
            public Observable<Category> call(Void aVoid) {
                return Observable.just(category);
            }
        });
    }

    private Observable<Void> deleteNewsItemFile(@NonNull String filePath) {
        StorageReference newsStorageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(newsStorageReference);
    }
}