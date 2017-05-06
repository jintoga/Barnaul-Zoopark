package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/2/2017.
 */

interface SpeciesEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<SpeciesEditorContract.View> {
        void createSpecies(@NonNull String name, @NonNull String description,
            @NonNull String categoryUid, @Nullable Uri iconUri);
    }
}
