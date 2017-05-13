package com.dat.barnaulzoopark.ui.animalsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

/**
 * Created by DAT on 5/12/2017.
 */

class AnimalsDetailFragmentPresenter extends MvpBasePresenter<AnimalsDetailContract.ViewFragment>
    implements AnimalsDetailContract.FragmentUserActionListener {

    private FirebaseDatabase database;

    AnimalsDetailFragmentPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void updateUserData(boolean isAlreadySubscribed, @NonNull User user,
        @NonNull Animal selectedAnimal) {
        if (isAlreadySubscribed) {
            user.getSubscribedAnimals().remove(selectedAnimal.getUid());
        } else {
            user.getSubscribedAnimals().put(selectedAnimal.getUid(), selectedAnimal.getUid());
        }
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.users);
        DatabaseReference userItemReference = databaseReference.child(user.getUid());
        userItemReference.setValue(user);
        if (getView() != null) {
            getView().onUpdatingUserData();
        }
        RxFirebaseDatabase.observeSingleValueEvent(userItemReference, Animal.class)
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onUpdateUserDataSuccess(!isAlreadySubscribed, selectedAnimal);
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onUpdateUserDataError(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }
}
