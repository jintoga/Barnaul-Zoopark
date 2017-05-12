package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by DAT on 5/12/2017.
 */

class FavoriteAnimalsPresenter extends MvpBasePresenter<FavoriteAnimalsContract.View>
    implements FavoriteAnimalsContract.UserActionListener {

    private FirebaseDatabase database;

    FavoriteAnimalsPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void loadFavoritesAnimals(@NonNull Set<String> animalUids) {
        database.getReference()
            .child(BZFireBaseApi.news)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Animal> animals = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (animalUids.contains(dataSnapshot.getKey())) {
                            Animal animal = snapshot.getValue(Animal.class);
                            animals.add(animal);
                        }
                    }
                    if (getView() != null) {
                        getView().bindAnimals(animals);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
