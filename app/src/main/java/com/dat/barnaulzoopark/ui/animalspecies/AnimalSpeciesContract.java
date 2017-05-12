package com.dat.barnaulzoopark.ui.animalspecies;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 4/16/2017.
 */

interface AnimalSpeciesContract {
    interface View extends MvpView {
        void bindAnimals(@NonNull List<Animal> animals);
    }

    interface UserActionListener extends MvpPresenter<View> {
        void loadAnimals(@NonNull String speciesUid);
    }
}
