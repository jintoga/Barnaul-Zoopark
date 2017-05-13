package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import java.util.Set;

/**
 * Created by DAT on 5/12/2017.
 */

interface FavoriteAnimalsContract {
    interface View extends MvpView {
        void bindAnimals(@NonNull List<Animal> favoriteAnimals);

        void onUpdatingUserData();

        void onUpdateUserDataError(@NonNull String localizedMessage);

        void onUpdateUserDataSuccess(boolean isAlreadySubscribed, int clickedPosition);
    }

    interface UserActionListener extends MvpPresenter<FavoriteAnimalsContract.View> {
        void loadFavoritesAnimals(@NonNull Set<String> animalUids);

        void updateUserData(boolean isAlreadySubscribed, @NonNull User user, @NonNull Animal animal,
            int clickedPosition);
    }
}
