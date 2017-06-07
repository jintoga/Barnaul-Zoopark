package com.dat.barnaulzoopark.ui.animalsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public void loadSponsors(@NonNull Set<String> sponsorUids) {
        if (getView() != null) {
            getView().onLoadProgress();
        }
        database.getReference(BZFireBaseApi.sponsors)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Sponsor> sponsors = new ArrayList<>();
                    List<Sponsor> result = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Sponsor sponsor = snapshot.getValue(Sponsor.class);
                        sponsors.add(sponsor);
                    }
                    for (Sponsor sponsor : sponsors) {
                        if (sponsorUids.contains(sponsor.getUid())) {
                            result.add(sponsor);
                        }
                    }
                    if (getView() != null) {
                        getView().bindSponsors(result);
                        getView().onLoadSuccess();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().onLoadError(databaseError.getMessage());
                    }
                }
            });
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
