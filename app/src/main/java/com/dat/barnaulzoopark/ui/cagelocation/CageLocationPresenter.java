package com.dat.barnaulzoopark.ui.cagelocation;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

class CageLocationPresenter extends MvpBasePresenter<CageLocationContract.View>
    implements CageLocationContract.UserActionListener {
    private FirebaseDatabase database;

    CageLocationPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void loadAnimals() {
        DatabaseReference animalReference = database.getReference(BZFireBaseApi.animal);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        animalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Animal> animals = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Animal animal = snapshot.getValue(Animal.class);
                    animals.add(animal);
                }
                if (getView() != null) {
                    getView().bindAnimals(animals);
                    getView().onLoadAnimalsSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (getView() != null) {
                    getView().onLoadAnimalsError(databaseError.getMessage());
                }
            }
        });
    }
}
