package com.dat.barnaulzoopark.ui.bloganimaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/13/2017.
 */

class BlogAnimalEditorPresenter extends MvpBasePresenter<BlogAnimalEditorContract.View>
    implements BlogAnimalEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    BlogAnimalEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createAnimal(@NonNull String title, @NonNull String description,
        @NonNull String animalUid, @Nullable Uri iconUri, @NonNull List<Attachment> attachments,
        @NonNull String videoUrl) {

    }

    @Override
    public void loadAnimals() {
        database.getReference(BZFireBaseApi.animal).addValueEventListener(new ValueEventListener() {
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
                if (getView() != null) {
                    getView().onLoadAnimalsError(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void loadSelectedBlog(@NonNull String selectedBlogUid) {

    }
}
