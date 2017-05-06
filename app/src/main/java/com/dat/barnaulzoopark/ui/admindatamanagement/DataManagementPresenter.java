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
        remove(data);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractData> void remove(@NonNull final T data) {
        DatabaseReference databaseReference;
        Class clazz;
        if (data instanceof Animal) {
            databaseReference = database.getReference(BZFireBaseApi.animal);
            clazz = Animal.class;
        } else if (data instanceof Species) {
            databaseReference = database.getReference(BZFireBaseApi.animal_species);
            clazz = Species.class;
        } else if (data instanceof Category) {
            databaseReference = database.getReference(BZFireBaseApi.animal_categories);
            clazz = Category.class;
        } else {
            return;
        }
        final DatabaseReference childDatabaseReference = databaseReference.child(data.getId());
        if (getView() != null) {
            getView().showRemoveProgress();
        }
        final DatabaseReference finalDatabaseReference = databaseReference;
        RxFirebaseDatabase.observeSingleValueEvent(childDatabaseReference, clazz)
            .flatMap(new Func1<T, Observable<T>>() {
                @Override
                public Observable<T> call(T data) {
                    return getDeleteIconObservable(data);
                }
            })
            .doOnNext(new Action1<T>() {
                @Override
                public void call(T data) {
                    deleteUidInChild(data);
                }
            })
            .subscribe(new Observer<T>() {
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
                public void onNext(T data) {
                    finalDatabaseReference.child(data.getId()).removeValue();
                }
            });
    }

    private <T extends AbstractData> void deleteUidInChild(@NonNull final T data) {
        DatabaseReference databaseReference;
        if (data instanceof Species) {
            //Species's child is Animal
            databaseReference = database.getReference(BZFireBaseApi.animal);
        } else if (data instanceof Category) {
            //Category's child is Species
            databaseReference = database.getReference(BZFireBaseApi.animal_species);
        } else {
            return;
        }
        final DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    deleteChildUid(data, snapshot, finalDatabaseReference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private <T extends AbstractData> void deleteChildUid(T data, DataSnapshot snapshot,
        DatabaseReference finalDatabaseReference) {
        if (data instanceof Species) {
            Animal animal = snapshot.getValue(Animal.class);
            if (animal.getSpeciesUid() != null && animal.getSpeciesUid().equals(data.getId())) {
                animal.setSpeciesUid(null);
                finalDatabaseReference.child(animal.getUid()).setValue(animal);
            }
        } else if (data instanceof Category) {
            Species species = snapshot.getValue(Species.class);
            if (species.getCategoryUid() != null && species.getCategoryUid().equals(data.getId())) {
                species.setCategoryUid(null);
                finalDatabaseReference.child(species.getUid()).setValue(species);
            }
        }
    }

    private <T extends AbstractData> Observable getDeleteIconObservable(@NonNull final T data) {
        String prefix;
        if (data instanceof Animal) {
            prefix = BZFireBaseApi.animal;
        } else if (data instanceof Species) {
            prefix = BZFireBaseApi.animal_species;
        } else {
            prefix = BZFireBaseApi.animal_categories;
        }
        String filePath = prefix + "/" + data.getId() + "/" + "icon";

        if (data.getPhotoUrl() == null || data.getPhotoUrl().isEmpty()) {
            return Observable.just(data);
        }
        return deleteFile(filePath).flatMap(new Func1<Void, Observable<T>>() {
            @Override
            public Observable<T> call(Void aVoid) {
                return Observable.just(data);
            }
        });
    }

    private Observable<Void> deleteFile(@NonNull String filePath) {
        StorageReference storageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(storageReference);
    }
}
