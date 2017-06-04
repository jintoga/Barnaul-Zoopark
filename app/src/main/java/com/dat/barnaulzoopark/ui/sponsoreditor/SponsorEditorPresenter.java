package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.support.annotation.NonNull;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 6/4/2017.
 */

class SponsorEditorPresenter extends MvpBasePresenter<SponsorEditorContract.View>
    implements SponsorEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    SponsorEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void loadSelectedSponsor(@NonNull String selectedSponsorUid) {

    }
}
