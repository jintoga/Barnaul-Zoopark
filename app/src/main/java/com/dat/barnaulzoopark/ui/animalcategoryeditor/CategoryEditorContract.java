package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/23/2017.
 */

public interface CategoryEditorContract {
    interface View extends MvpView {
        void highlightRequiredFields();

        void showCreatingProgress();
    }

    interface UserActionListener extends MvpPresenter<CategoryEditorContract.View> {
        void createCategory(@NonNull String name, @NonNull String description,
            @Nullable Uri iconUri);
    }
}
