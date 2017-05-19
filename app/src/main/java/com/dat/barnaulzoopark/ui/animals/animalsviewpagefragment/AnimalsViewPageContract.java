package com.dat.barnaulzoopark.ui.animals.animalsviewpagefragment;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.animal.Species;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 4/9/2017.
 */

interface AnimalsViewPageContract {
    interface View extends MvpView {
        void bindSpecies(@NonNull List<Species> categories);
    }

    interface UserActionListener extends MvpPresenter<AnimalsViewPageContract.View> {
        void loadSpecies(@NonNull String categoryUid);
    }
}
