package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.Query;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 5/2/2017.
 */

interface SpeciesEditorContract {
    interface View extends MvpView {
        void bindCategories(@NonNull List<Category> categories);

        void bindSelectedSpecies(@NonNull Species selectedSpecies);

        void onCreatingSpeciesFailure(@NonNull String localizedMessage);

        void onCreatingSpeciesSuccess();

        void onCreatingComplete();

        void onLoadCategoriesError(@NonNull String localizedMessage);

        void onLoadCategoriesSuccess();

        void onLoadSpeciesError(@NonNull String localizedMessage);

        void onLoadSpeciesSuccess();

        void onUploadFailure(@NonNull String localizedMessage);

        void showCreatingProgress();

        void highlightRequiredFields();

        void showLoadingProgress();

        void uploadingIconProgress();
    }

    interface UserActionListener extends MvpPresenter<SpeciesEditorContract.View> {
        void createSpecies(@NonNull String name, @NonNull String description,
            @NonNull String categoryUid, @Nullable Uri iconUri);

        void loadCategories();

        Query getChildAnimalsReference(@Nullable String selectedSpeciesUid);

        void loadSelectedSpecies(@NonNull String selectedSpeciesUid);
    }
}
