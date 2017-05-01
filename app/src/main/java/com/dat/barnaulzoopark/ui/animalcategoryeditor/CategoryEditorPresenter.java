package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorPresenter extends MvpBasePresenter<CategoryEditorContract.View>
    implements CategoryEditorContract.UserActionListener {

    @Override
    public void createCategory(@NonNull String name, @NonNull String description,
        @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(description)) {
            if (getView() != null) {
                getView().showCreatingProgress();
            }
            createNews(name, description, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void createNews(@NonNull String name, @NonNull String description,
        @Nullable Uri iconUri) {

    }
}
