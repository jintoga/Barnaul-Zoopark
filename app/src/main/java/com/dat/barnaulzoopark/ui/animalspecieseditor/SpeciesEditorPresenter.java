package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by DAT on 5/2/2017.
 */

class SpeciesEditorPresenter extends MvpBasePresenter<SpeciesEditorContract.View>
    implements SpeciesEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    SpeciesEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createSpecies(@NonNull String name, @NonNull String description,
        @NonNull String categoryUid, @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(description) && !"".equals(categoryUid)) {
            if (getView() != null) {
                getView().showCreatingProgress();
            }
            create(name, description, categoryUid, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void create(@NonNull String name, @NonNull String description,
        @NonNull String categoryUid, @Nullable final Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference speciesItemReference = databaseReference.child(uid);
        Species species = new Species(uid, name, description, categoryUid);
        speciesItemReference.setValue(species);
        RxFirebaseDatabase.observeSingleValueEvent(speciesItemReference, Species.class)
            .subscribe(species1 -> {
                if (getView() != null) {
                    getView().onCreatingSpeciesSuccess();
                }
                if (species1 != null) {
                    uploadIcon(species1, iconUri);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onCreatingSpeciesFailure(throwable.getLocalizedMessage());
                }
            });
    }

    private void uploadIcon(@NonNull Species species, @Nullable Uri iconUri) {
        if (iconUri == null) {
            if (getView() != null) {
                getView().onCreatingComplete();
            }
        } else {
            getObservableUploadIcon(species, iconUri).subscribe(new Observer<Species>() {
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
                public void onNext(Species species) {

                }
            });
        }
    }

    private Observable<Species> getObservableUploadIcon(@NonNull final Species species,
        @NonNull Uri uri) {
        if (getView() != null) {
            getView().uploadingIconProgress();
        }
        String filePath = BZFireBaseApi.animal_species + "/" + species.getUid() + "/" + "icon";
        return uploadImage(filePath, uri).flatMap(new Func1<Uri, Observable<Species>>() {
            @Override
            public Observable<Species> call(Uri uploadedUri) {
                return setIconToSpecies(species.getUid(), uploadedUri);
            }
        });
    }

    private Observable<Species> setIconToSpecies(@NonNull String uid, @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        DatabaseReference speciesReference = databaseReference.child(uid);
        speciesReference.child("icon").setValue(uploadedUri.toString());
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference, Species.class);
    }

    private Observable<Uri> uploadImage(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference speciesStorageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(speciesStorageReference, uri)
            .flatMap(new Func1<UploadTask.TaskSnapshot, Observable<Uri>>() {
                @Override
                public Observable<Uri> call(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uploadedUri = taskSnapshot.getDownloadUrl();
                    return Observable.just(uploadedUri);
                }
            });
    }

    @NonNull
    @Override
    public DatabaseReference getCategoryReference() {
        return database.getReference(BZFireBaseApi.animal_categories);
    }

    @Override
    public Query getChildAnimalsReference(@Nullable String selectedSpeciesUid) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.animal);
        return databaseReference.orderByChild("speciesUid").equalTo(selectedSpeciesUid);
    }
}
