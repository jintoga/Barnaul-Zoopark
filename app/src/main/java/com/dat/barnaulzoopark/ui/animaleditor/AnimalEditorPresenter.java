package com.dat.barnaulzoopark.ui.animaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.List;

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
        @NonNull String speciesUid, int gender, @Nullable Uri iconUri, @Nullable Uri bannerImageUri,
        @NonNull List<Attachment> attachments, @NonNull String videoUrl) {
        if (!"".equals(name) && !"".equals(aboutAnimal)) {
            if (getView() != null) {
                getView().creatingProgress();
            }
            create(name, aboutAnimal, speciesUid, gender, iconUri, bannerImageUri, attachments,
                videoUrl);
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
        @NonNull String speciesUid, int gender, @Nullable Uri iconUri, @Nullable Uri bannerImageUri,
        @NonNull List<Attachment> attachments, @NonNull String videoUrl) {

    }
}
