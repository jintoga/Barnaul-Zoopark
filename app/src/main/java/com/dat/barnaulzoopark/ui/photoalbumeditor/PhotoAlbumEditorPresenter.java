package com.dat.barnaulzoopark.ui.photoalbumeditor;

import android.support.annotation.NonNull;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 5/21/2017.
 */

class PhotoAlbumEditorPresenter extends MvpBasePresenter<PhotoAlbumEditorContract.View>
    implements PhotoAlbumEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    PhotoAlbumEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void loadSelectedPhotoAlbum(@NonNull String selectedPhotAlbumUid) {

    }
}
