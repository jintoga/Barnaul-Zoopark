package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.Query;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/23/2017.
 */

interface CategoryEditorContract {
    interface View extends MvpView {
        void bindSelectedCategory(@NonNull Category selectedCategory);

        void highlightRequiredFields();

        void onCreatingCategoryFailure(@NonNull String localizedMessage);

        void onCreatingSuccess();

        void onEditError(@NonNull String localizedMessage);

        void onEditSuccess();

        void onLoadCategoryError(@NonNull String localizedMessage);

        void onLoadCategorySuccess();

        void onLoadChildrenSpeciesError(@NonNull String message);

        void onRemoveChildError(@NonNull String message);

        void showCreatingProgress();

        void showEditingProgress();

        void showLoadingProgress();

        void showSpeciesChildrenHeader(boolean shouldShow);
    }

    interface UserActionListener extends MvpPresenter<CategoryEditorContract.View> {
        void createCategory(@NonNull String name, @NonNull String description,
            @Nullable Uri iconUri);

        void editCategory(@NonNull Category selectedCategory, @NonNull String name,
            @NonNull String description, @Nullable Uri iconUri);

        @NonNull
        Query getChildSpeciesReference(@Nullable String selectedCategoryUid);

        void loadSelectedCategory(@NonNull String selectedCategoryUid);

        void removeChildFromCategory(@NonNull Species species);
    }
}
