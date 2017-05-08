package com.dat.barnaulzoopark.ui.animaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by DAT on 3/11/2017.
 */

class AnimalEditorPresenter extends MvpBasePresenter<AnimalEditorContract.View>
    implements AnimalEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    AnimalEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createAnimal(@NonNull String name, @NonNull String aboutAnimal,
        @NonNull String speciesUid, boolean gender, @Nullable Date dateOfBirth,
        @Nullable Uri iconUri, @Nullable Uri bannerImageUri, @NonNull List<Attachment> attachments,
        @NonNull String videoUrl) {
        if (!"".equals(name) && !"".equals(aboutAnimal)) {
            create(name, aboutAnimal, speciesUid, gender, dateOfBirth, iconUri, bannerImageUri,
                attachments, videoUrl);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    @NonNull
    @Override
    public DatabaseReference getSpeciesReference() {
        return database.getReference(BZFireBaseApi.animal_species);
    }

    private void create(@NonNull String name, @NonNull String aboutAnimal,
        @NonNull String speciesUid, boolean gender, @Nullable Date dateOfBirth,
        @Nullable Uri iconUri, @Nullable Uri bannerImageUri, @NonNull List<Attachment> attachments,
        @NonNull String videoUrl) {
        DatabaseReference animalDatabaseReference =
            database.getReference().child(BZFireBaseApi.animal);
        final String uid = animalDatabaseReference.push().getKey();
        final DatabaseReference animalItemReference = animalDatabaseReference.child(uid);
        Animal animal = new Animal(uid, name, speciesUid, aboutAnimal, gender,
            Calendar.getInstance().getTimeInMillis());
        if (!"".equals(videoUrl)) {
            animal.setVideo(videoUrl);
        }
        if (dateOfBirth != null) {
            animal.setDateOfBirth(dateOfBirth.getTime());
        }
        animalItemReference.setValue(animal);
        if (getView() != null) {
            getView().creatingProgress();
        }
        final String filePath = BZFireBaseApi.animal + "/" + animal.getUid() + "/";
        RxFirebaseDatabase.observeSingleValueEvent(animalItemReference, Animal.class)
            .doOnNext(this::addAnimalToSpecies)
            .flatMap(animal1 -> {
                String path = filePath + "photoSmall";
                return uploadImage(animal1, iconUri, path, "photoSmall");
            })
            .flatMap(animal1 -> {
                String path = filePath + "photoBig";
                return uploadImage(animal1, bannerImageUri, path, "photoBig");
            })
            //ToDo: implement upload attachments
            .subscribe(animal1 -> {
            }, throwable -> {
                if (getView() != null) {
                    getView().onCreatingFailure(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onCreatingSuccess();
                }
            });
    }

    private void addAnimalToSpecies(@NonNull Animal animal) {
        DatabaseReference speciesDatabaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        final DatabaseReference speciesItemReference =
            speciesDatabaseReference.child(animal.getSpeciesUid());
        RxFirebaseDatabase.observeSingleValueEvent(speciesItemReference, Species.class)
            .doOnNext(species -> {
                species.getAnimals().put(animal.getUid(), animal.getUid());
                speciesDatabaseReference.child(animal.getSpeciesUid()).setValue(species);
            })
            .subscribe();
    }

    @NonNull
    private Observable<Animal> uploadImage(final @NonNull Animal animal, @Nullable Uri uri,
        @Nullable String filePath, @Nullable String field) {
        if (uri == null || filePath == null || field == null) {
            return Observable.just(animal);
        }
        return uploadFile(filePath, uri).flatMap(new Func1<Uri, Observable<Animal>>() {
            @Override
            public Observable<Animal> call(Uri uploadedUri) {
                return setImagePathToItem(animal, uploadedUri, field);
            }
        });
    }

    @NonNull
    private Observable<Uri> uploadFile(@NonNull final String fileName, @NonNull Uri uri) {
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

    @NonNull
    private Observable<Animal> setImagePathToItem(@NonNull Animal animal, @NonNull Uri uploadedUri,
        @NonNull String field) {
        DatabaseReference animalDatabaseReference =
            database.getReference().child(BZFireBaseApi.animal);
        DatabaseReference animalItemReference = animalDatabaseReference.child(animal.getUid());
        animalItemReference.child(field).setValue(uploadedUri.toString());
        return Observable.just(animal);
    }
}
