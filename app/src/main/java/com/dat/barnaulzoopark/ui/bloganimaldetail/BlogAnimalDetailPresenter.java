package com.dat.barnaulzoopark.ui.bloganimaldetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 5/15/2017.
 */

class BlogAnimalDetailPresenter extends MvpBasePresenter<BlogAnimalDetailContract.View>
    implements BlogAnimalDetailContract.UserActionListener {

    private FirebaseDatabase database;

    BlogAnimalDetailPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void loadBlogAnimal(@NonNull String blogUid) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.blog_animal).child(blogUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BlogAnimal blogAnimal = dataSnapshot.getValue(BlogAnimal.class);
                if (getView() != null) {
                    getView().showBlogAnimal(blogAnimal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
