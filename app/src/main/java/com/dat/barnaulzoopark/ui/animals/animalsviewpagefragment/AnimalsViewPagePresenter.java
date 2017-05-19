package com.dat.barnaulzoopark.ui.animals.animalsviewpagefragment;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 4/9/2017.
 */

class AnimalsViewPagePresenter extends MvpBasePresenter<AnimalsViewPageContract.View>
    implements AnimalsViewPageContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    AnimalsViewPagePresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void loadSpecies(@NonNull String categoryUid) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_species);
        Query query = databaseReference.orderByChild("categoryUid").equalTo(categoryUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Species> speciesList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Species species = snapshot.getValue(Species.class);
                    speciesList.add(species);
                }
                if (getView() != null) {
                    getView().bindSpecies(speciesList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
