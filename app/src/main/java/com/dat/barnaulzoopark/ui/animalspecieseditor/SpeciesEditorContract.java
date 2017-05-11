package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.animal.Animal;
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

        void onCreatingComplete();

        void onRemoveChildError(@NonNull String message);

        void showCreatingProgress();

        void onLoadCategoriesError(@NonNull String localizedMessage);

        void onLoadSpeciesError(@NonNull String localizedMessage);

        void onLoadSpeciesSuccess();

        void highlightRequiredFields();

        void showEditingProgress();

        void onEditError(@NonNull String localizedMessage);

        void onEditSuccess();

        void showLoadingProgress();

        void showSpeciesChildrenHeader(boolean shouldShow);
    }

    interface UserActionListener extends MvpPresenter<SpeciesEditorContract.View> {
        void createSpecies(@NonNull String name, @NonNull String description,
            @NonNull String categoryUid, @Nullable Uri iconUri);

        void editSpecies(@NonNull Species selectedSpecies, @NonNull String name,
            @NonNull String description, @NonNull String categoryUid, @Nullable Uri iconUri);

        void loadCategories();

        @NonNull
        Query getChildAnimalsReference(@Nullable String selectedSpeciesUid);

        void loadSelectedSpecies(@NonNull String selectedSpeciesUid);

        void removeChildFromSpecies(@NonNull Animal animal);
    }
}
