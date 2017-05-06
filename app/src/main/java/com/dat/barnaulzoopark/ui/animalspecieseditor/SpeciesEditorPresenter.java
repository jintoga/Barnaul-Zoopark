package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

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

    }

    @NonNull
    @Override
    public DatabaseReference getCategoryReference() {
        return database.getReference(BZFireBaseApi.animal_categories);
    }
}
