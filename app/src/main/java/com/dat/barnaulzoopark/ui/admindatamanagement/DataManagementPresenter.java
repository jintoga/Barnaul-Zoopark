package com.dat.barnaulzoopark.ui.admindatamanagement;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementPresenter extends MvpBasePresenter<DataManagementContract.View>
    implements DataManagementContract.UserActionListener {

    private FirebaseDatabase database;

    DataManagementPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public DatabaseReference getDataReference(String referenceName) {
        return database.getReference(referenceName);
    }
}
