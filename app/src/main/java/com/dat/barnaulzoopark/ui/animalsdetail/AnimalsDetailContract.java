package com.dat.barnaulzoopark.ui.animalsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 4/9/2017.
 */

interface AnimalsDetailContract {
    interface View extends MvpView {
        void bindAnimals(@NonNull List<Animal> animals);
    }

    interface UserActionListener extends MvpPresenter<AnimalsDetailContract.View> {
        void loadAnimals(@NonNull String speciesUid);
    }
}
