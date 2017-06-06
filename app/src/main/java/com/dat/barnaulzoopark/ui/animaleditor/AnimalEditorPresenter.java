package com.dat.barnaulzoopark.ui.animaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.utils.UriUtil;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rx.Observable;

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
        @Nullable Uri iconUri, @Nullable Uri bannerImageUri, @Nullable Uri habitatMapImageUri,
        @NonNull List<Attachment> attachments, @NonNull String videoUrl, @Nullable Double lat,
        @Nullable Double lng, @Nullable List<String> animalSponsorUids) {
        if (!"".equals(name) && !"".equals(aboutAnimal)) {
            create(name, aboutAnimal, speciesUid, gender, dateOfBirth, iconUri, bannerImageUri,
                habitatMapImageUri, attachments, videoUrl, lat, lng, animalSponsorUids);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    @Override
    public void editAnimal(@NonNull Animal selectedAnimal, @NonNull String name,
        @NonNull String aboutAnimal, @NonNull String speciesUid, boolean gender,
        @Nullable Date dateOfBirth, @Nullable Uri iconUri, @Nullable Uri bannerImageUri,
        @Nullable Uri habitatMapImageUri, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete, @NonNull String videoUrl,
        @Nullable Double lat, @Nullable Double lng, @Nullable List<String> animalSponsorUids) {
        if (!"".equals(name) && !"".equals(aboutAnimal)) {
            edit(selectedAnimal, name, aboutAnimal, speciesUid, gender, dateOfBirth, iconUri,
                bannerImageUri, habitatMapImageUri, attachmentsToAdd, attachmentsToDelete, videoUrl,
                lat, lng, animalSponsorUids);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void edit(@NonNull Animal selectedAnimal, @NonNull String name,
        @NonNull String aboutAnimal, @NonNull String speciesUid, boolean gender,
        @Nullable Date dateOfBirth, @Nullable Uri iconUri, @Nullable Uri bannerImageUri,
        @Nullable Uri habitatMapImageUri, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete, @NonNull String videoUrl,
        @Nullable Double lat, @Nullable Double lng, @Nullable List<String> animalSponsorUids) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.animal);
        final DatabaseReference animalItemReference =
            databaseReference.child(selectedAnimal.getUid());
        selectedAnimal.update(speciesUid, name, aboutAnimal, gender);
        if (!"".equals(videoUrl)) {
            selectedAnimal.setVideo(videoUrl);
        }
        if (dateOfBirth != null) {
            selectedAnimal.setDateOfBirth(dateOfBirth.getTime());
        }
        if (lat != null && lng != null) {
            selectedAnimal.setLatLng(lat, lng);
        }
        selectedAnimal.getSponsors().clear();
        if (animalSponsorUids != null && !animalSponsorUids.isEmpty()) {
            for (String sponsorUid : animalSponsorUids) {
                selectedAnimal.getSponsors().put(sponsorUid, sponsorUid);
            }
        }
        animalItemReference.setValue(selectedAnimal);
        if (getView() != null) {
            getView().showEditingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(animalItemReference, Animal.class)
            .flatMap(
                animal -> updateIcon(selectedAnimal, iconUri, animalItemReference, "photoSmall",
                    selectedAnimal.getPhotoSmall() != null))
            .flatMap(animal -> updateIcon(selectedAnimal, bannerImageUri, animalItemReference,
                "photoBig", selectedAnimal.getPhotoBig() != null))
            .flatMap(animal -> updateIcon(selectedAnimal, habitatMapImageUri, animalItemReference,
                "imageHabitatMap", selectedAnimal.getImageHabitatMap() != null))
            .flatMap(
                animal -> updateAttachments(selectedAnimal, animalItemReference, attachmentsToAdd,
                    attachmentsToDelete))
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
    private Observable<Animal> updateAttachments(@NonNull Animal selectedAnimal,
        @NonNull DatabaseReference animalItemReference, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {
        return Observable.concat(
            deleteAttachments(selectedAnimal, animalItemReference, attachmentsToDelete),
            uploadAttachments(selectedAnimal, attachmentsToAdd));
    }

    @NonNull
    private Observable<Animal> deleteAttachments(@NonNull Animal selectedAnimal,
        @NonNull DatabaseReference animalItemReference,
        @NonNull List<Attachment> attachmentsToDelete) {
        return Observable.from(attachmentsToDelete)
            .filter(attachment -> attachment.getAttachmentUid() != null)
            .flatMap(attachment -> {
                final String attachmentPath = BZFireBaseApi.animal
                    + "/"
                    + selectedAnimal.getUid()
                    + "/photos/"
                    + attachment.getAttachmentUid();
                return deleteAttachment(attachmentPath).flatMap(aVoid -> {
                    selectedAnimal.getPhotos().remove(attachment.getAttachmentUid());
                    animalItemReference.setValue(selectedAnimal);
                    return Observable.just(selectedAnimal);
                });
            });
    }

    @NonNull
    private Observable<Void> deleteAttachment(@NonNull String filePath) {
        StorageReference animalStorageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(animalStorageReference);
    }

    @NonNull
    private Observable<Animal> updateIcon(@NonNull Animal selectedAnimal, @Nullable Uri iconUri,
        @NonNull DatabaseReference animalItemReference, @Nullable String field,
        boolean alreadyHasImage) {
        String filePath = BZFireBaseApi.animal + "/" + selectedAnimal.getUid() + "/" + field;
        if (iconUri == null && alreadyHasImage) {
            //delete
            return deleteImage(selectedAnimal, animalItemReference, filePath, field);
        } else if (iconUri != null && UriUtil.isLocalFile(iconUri)) {
            //update
            return uploadImage(selectedAnimal, iconUri, filePath, field);
        } else {
            return Observable.just(selectedAnimal);
        }
    }

    @NonNull
    private Observable<Animal> deleteImage(@NonNull Animal selectedAnimal,
        @NonNull DatabaseReference animalItemReference, @NonNull String filePath, String field) {
        StorageReference animalStorageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(animalStorageReference).flatMap(aVoid -> {
            switch (field) {
                case "photoSmall":
                    selectedAnimal.clearPhotoSmall();
                    break;
                case "photoBig":
                    selectedAnimal.clearPhotoBig();
                    break;
                case "imageHabitatMap":
                    selectedAnimal.clearImageHabitatMap();
                    break;
                default:
                    return Observable.just(selectedAnimal);
            }
            animalItemReference.setValue(selectedAnimal);
            return Observable.just(selectedAnimal);
        });
    }

    @Override
    public void loadSelectedAnimal(@NonNull String selectedAnimalUid) {
        DatabaseReference animalReference =
            database.getReference(BZFireBaseApi.animal).child(selectedAnimalUid);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(animalReference, Animal.class)
            .subscribe(selectedAnimal -> {
                if (getView() != null) {
                    getView().bindSelectedAnimal(selectedAnimal);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onLoadAnimalError(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onLoadAnimalSuccess();
                }
            });
    }

    @Override
    public void loadSpecies() {
        database.getReference(BZFireBaseApi.animal_species)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Species> speciesList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Species species = snapshot.getValue(Species.class);
                        speciesList.add(species);
                    }
                    if (getView() != null) {
                        getView().bindSpecies(speciesList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().onLoadError(databaseError.getMessage());
                    }
                }
            });
    }

    @Override
    public void loadSponsors() {
        database.getReference(BZFireBaseApi.sponsors)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Sponsor> sponsors = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Sponsor sponsor = snapshot.getValue(Sponsor.class);
                        sponsors.add(sponsor);
                    }
                    if (getView() != null) {
                        getView().bindSponsors(sponsors);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().onLoadError(databaseError.getMessage());
                    }
                }
            });
    }

    private void create(@NonNull String name, @NonNull String aboutAnimal,
        @NonNull String speciesUid, boolean gender, @Nullable Date dateOfBirth,
        @Nullable Uri iconUri, @Nullable Uri bannerImageUri, @Nullable Uri habitatMapImageUri,
        @NonNull List<Attachment> attachments, @NonNull String videoUrl, @Nullable Double lat,
        @Nullable Double lng, @Nullable List<String> animalSponsorUids) {
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
        if (lat != null && lng != null) {
            animal.setLatLng(lat, lng);
        }
        if (animalSponsorUids != null && !animalSponsorUids.isEmpty()) {
            for (String sponsorUid : animalSponsorUids) {
                animal.getSponsors().put(sponsorUid, sponsorUid);
            }
        }
        animalItemReference.setValue(animal);
        if (getView() != null) {
            getView().creatingProgress();
        }
        final String filePath = BZFireBaseApi.animal + "/" + animal.getUid() + "/";
        RxFirebaseDatabase.observeSingleValueEvent(animalItemReference, Animal.class)
            .flatMap(animal1 -> {
                String path = filePath + "photoSmall";
                return uploadImage(animal1, iconUri, path, "photoSmall");
            })
            .flatMap(animal1 -> {
                String path = filePath + "photoBig";
                return uploadImage(animal1, bannerImageUri, path, "photoBig");
            })
            .flatMap(animal1 -> {
                String path = filePath + "imageHabitatMap";
                return uploadImage(animal1, habitatMapImageUri, path, "imageHabitatMap");
            })
            .flatMap(animal1 -> uploadAttachments(animal1, attachments))
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

    @NonNull
    private Observable<Animal> uploadAttachments(@NonNull Animal animal,
        @NonNull List<Attachment> attachments) {
        return Observable.from(attachments)
            .filter(
                (attachment1) -> attachment1.isFilled() && attachment1.getAttachmentUid() == null)
            .flatMap(attachment -> {
                Uri uri = Uri.parse(attachment.getUrl());
                return uploadAttachmentImage(animal, uri);
            });
    }

    @NonNull
    private Observable<Animal> uploadAttachmentImage(@NonNull Animal animal, @NonNull Uri uri) {
        final String attachmentUid = database.getReference().push().getKey();
        final String attachmentPath =
            BZFireBaseApi.animal + "/" + animal.getUid() + "/photos/" + attachmentUid;
        return uploadFile(attachmentPath, uri).flatMap(
            uploadedUri -> setAttachmentToAnimal(animal, attachmentUid, uploadedUri));
    }

    @NonNull
    private Observable<Animal> setAttachmentToAnimal(@NonNull Animal animal,
        @NonNull String attachmentUid, Uri uploadedUri) {
        DatabaseReference animalDatabaseReference =
            database.getReference().child(BZFireBaseApi.animal);
        DatabaseReference animalItemReference = animalDatabaseReference.child(animal.getUid());
        DatabaseReference animalItemPhotoReference = animalItemReference.child("photos");
        animalItemPhotoReference.child(attachmentUid).setValue(uploadedUri.toString());
        return Observable.just(animal);
    }

    @NonNull
    private Observable<Animal> uploadImage(final @NonNull Animal animal, @Nullable Uri uri,
        @Nullable String filePath, @Nullable String field) {
        if (uri == null || filePath == null || field == null) {
            return Observable.just(animal);
        }
        return uploadFile(filePath, uri).flatMap(
            uploadedUri -> setImagePathToItem(animal, uploadedUri, field));
    }

    @NonNull
    private Observable<Uri> uploadFile(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference newsStorageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(newsStorageReference, uri)
            .flatMap(taskSnapshot -> Observable.just(taskSnapshot.getDownloadUrl()));
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
