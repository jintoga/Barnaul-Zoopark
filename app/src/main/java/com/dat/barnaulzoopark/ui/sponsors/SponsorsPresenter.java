package com.dat.barnaulzoopark.ui.sponsors;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 6/4/2017.
 */

class SponsorsPresenter extends MvpBasePresenter<SponsorsContract.View>
    implements SponsorsContract.UserActionListener {

    private FirebaseDatabase database;

    SponsorsPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public DatabaseReference getSponsorsReference() {
        return database.getReference().child(BZFireBaseApi.sponsors);
    }
}
