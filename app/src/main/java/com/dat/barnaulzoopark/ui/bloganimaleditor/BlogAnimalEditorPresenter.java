package com.dat.barnaulzoopark.ui.bloganimaleditor;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

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
}
