package com.dat.barnaulzoopark.ui.animals.animalsfragment;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.animal.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 3/28/2017.
 */

class AnimalsPresenter extends MvpBasePresenter<AnimalsContract.View>
    implements AnimalsContract.UserActionListener {
    private FirebaseDatabase database;

    AnimalsPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void loadCategories() {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.animal_categories);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Category> categories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categories.add(category);
                }
                if (getView() != null) {
                    getView().bindCategories(categories);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
