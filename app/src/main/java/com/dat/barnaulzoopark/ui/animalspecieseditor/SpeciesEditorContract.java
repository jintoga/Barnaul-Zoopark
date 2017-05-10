package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/2/2017.
 */

interface SpeciesEditorContract {
    interface View extends MvpView {
        void onCreatingSpeciesFailure(@NonNull String localizedMessage);

        void onCreatingSpeciesSuccess();

        void onCreatingComplete();

        void onUploadFailure(@NonNull String localizedMessage);

        void showCreatingProgress();

        void highlightRequiredFields();

        void uploadingIconProgress();
    }

    interface UserActionListener extends MvpPresenter<SpeciesEditorContract.View> {
        void createSpecies(@NonNull String name, @NonNull String description,
            @NonNull String categoryUid, @Nullable Uri iconUri);

        @NonNull
        DatabaseReference getCategoryReference();

        Query getChildAnimalsReference(String selectedSpeciesUid);
    }
}
