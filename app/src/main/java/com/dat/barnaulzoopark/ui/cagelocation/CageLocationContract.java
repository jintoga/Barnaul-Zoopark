package com.dat.barnaulzoopark.ui.cagelocation;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

interface CageLocationContract {
    interface View extends MvpView {
        void onLoadAnimalsError(@NonNull String localizedMessage);

        void onLoadAnimalsSuccess();

        void showLoadingProgress();

        void bindAnimals(@NonNull List<Animal> animals);
    }

    interface UserActionListener extends MvpPresenter<CageLocationContract.View> {
        void loadAnimals();
    }
}
