package com.dat.barnaulzoopark.ui.animalsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 4/9/2017.
 */

class AnimalsDetailActivityPresenter extends MvpBasePresenter<AnimalsDetailContract.ViewActivity>
    implements AnimalsDetailContract.ActivityUserActionListener {

    private FirebaseDatabase database;

    AnimalsDetailActivityPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void loadAnimals(@NonNull String speciesUid) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.animal);
        Query query = databaseReference.orderByChild("speciesUid").equalTo(speciesUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Animal> animals = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Animal animal = snapshot.getValue(Animal.class);
                    animals.add(animal);
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
