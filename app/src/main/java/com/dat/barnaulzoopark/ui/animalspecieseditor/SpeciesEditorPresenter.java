package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.utils.UriUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
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
            create(name, description, categoryUid, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    @Override
    public void editSpecies(@NonNull Species selectedSpecies, @NonNull String name,
        @NonNull String description, @NonNull String categoryUid, @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(description)) {
            edit(selectedSpecies, categoryUid, name, description, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void edit(@NonNull Species selectedSpecies, @NonNull String categoryUid,
        @NonNull String name, @NonNull String description, @Nullable Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        final DatabaseReference speciesItemReference =
            databaseReference.child(selectedSpecies.getUid());
        selectedSpecies.update(categoryUid, name, description);
        speciesItemReference.setValue(selectedSpecies);
        if (getView() != null) {
            getView().showEditingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(speciesItemReference, Species.class)
            .flatMap(species -> updateIcon(selectedSpecies, iconUri, speciesItemReference))
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
    private Observable<Species> updateIcon(@NonNull Species selectedSpecies, @Nullable Uri iconUri,
        @NonNull DatabaseReference speciesItemReference) {
        String filePath =
            BZFireBaseApi.animal_species + "/" + selectedSpecies.getUid() + "/" + "icon";
        if (iconUri == null && selectedSpecies.getIcon() != null) {
            //delete
            return deleteIcon(selectedSpecies, speciesItemReference, filePath);
        } else if (iconUri != null && UriUtil.isLocalFile(iconUri)) {
            //update
            return uploadIcon(selectedSpecies, iconUri);
        } else {
            return Observable.just(selectedSpecies);
        }
    }

    @NonNull
    private Observable<Species> deleteIcon(@NonNull Species selectedSpecies,
        @NonNull DatabaseReference speciesItemReference, @NonNull String filePath) {
        StorageReference speciesStorageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(speciesStorageReference).flatMap(aVoid -> {
            selectedSpecies.clearIcon();
            speciesItemReference.setValue(selectedSpecies);
            return Observable.just(selectedSpecies);
        });
    }

    @Override
    public void loadCategories() {
        database.getReference(BZFireBaseApi.animal_categories)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Category> categories = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Category category = snapshot.getValue(Category.class);
                        categories.add(category);
                    }
                    if (getView() != null) {
                        getView().bindCategories(categories);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().onLoadCategoriesError(databaseError.getMessage());
                    }
                }
            });
    }

    private void create(@NonNull String name, @NonNull String description,
        @NonNull String categoryUid, @Nullable final Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference speciesItemReference = databaseReference.child(uid);
        Species species = new Species(uid, name, description, categoryUid);
        speciesItemReference.setValue(species);
        if (getView() != null) {
            getView().showCreatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(speciesItemReference, Species.class)
            .flatMap(species1 -> uploadIcon(species1, iconUri))
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onCreatingComplete();
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onCreatingSpeciesFailure(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }

    @NonNull
    private Observable<Species> uploadIcon(@NonNull Species species, @Nullable Uri iconUri) {
        if (iconUri == null) {
            return Observable.just(species);
        } else {
            return getObservableUploadIcon(species, iconUri);
        }
    }

    @NonNull
    private Observable<Species> getObservableUploadIcon(@NonNull final Species species,
        @NonNull Uri uri) {
        String filePath = BZFireBaseApi.animal_species + "/" + species.getUid() + "/" + "icon";
        return uploadImage(filePath, uri).flatMap(new Func1<Uri, Observable<Species>>() {
            @Override
            public Observable<Species> call(Uri uploadedUri) {
                return setIconToSpecies(species, uploadedUri);
            }
        });
    }

    @NonNull
    private Observable<Species> setIconToSpecies(@NonNull Species species,
        @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        DatabaseReference speciesReference = databaseReference.child(species.getUid());
        speciesReference.child("icon").setValue(uploadedUri.toString());
        return Observable.just(species);
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
    public Query getChildAnimalsReference(@Nullable String selectedSpeciesUid) {
        if (selectedSpeciesUid == null) {
            selectedSpeciesUid = "";
        }
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.animal);
        Query query = databaseReference.orderByChild("speciesUid").equalTo(selectedSpeciesUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getView() != null) {
                    getView().showSpeciesChildrenHeader(dataSnapshot.getChildrenCount() > 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return query;
    }

    @Override
    public void loadSelectedSpecies(@NonNull String selectedSpeciesUid) {
        DatabaseReference speciesReference =
            database.getReference(BZFireBaseApi.animal_species).child(selectedSpeciesUid);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(speciesReference, Species.class)
            .subscribe(selectedSpecies -> {
                if (getView() != null) {
                    getView().bindSelectedSpecies(selectedSpecies);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onLoadSpeciesError(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onLoadSpeciesSuccess();
                }
            });
    }

    @Override
    public void removeChildFromSpecies(@NonNull Animal animal) {
        animal.clearSpeciesUid();
        DatabaseReference databaseReference = database.getReference(BZFireBaseApi.animal);
        databaseReference.child(animal.getUid()).setValue(animal).addOnFailureListener(e -> {
            if (getView() != null) {
                getView().onRemoveChildError(e.getMessage());
            }
        });
    }
}
