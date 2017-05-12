package com.dat.barnaulzoopark.ui.animalsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 4/9/2017.
 */

interface AnimalsDetailContract {
    interface ViewActivity extends MvpView {
        void bindAnimals(@NonNull List<Animal> animals);
    }

    interface ActivityUserActionListener extends MvpPresenter<ViewActivity> {
        void loadAnimals(@NonNull String speciesUid);
    }

    interface ViewFragment extends MvpView {
        void onUpdatingUserData();

        void onUpdateUserDataError(@NonNull String localizedMessage);

        void onUpdateUserDataSuccess(boolean isAlreadySubscribed);
    }

    interface FragmentUserActionListener extends MvpPresenter<AnimalsDetailContract.ViewFragment> {
        void updateUserData(boolean isAlreadySubscribed, @NonNull User user,
            @NonNull Animal selectedAnimal);
    }
}
