package com.dat.barnaulzoopark.ui.admindatamanagement;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.AbstractData;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
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

    @Override
    public void removeItem(AbstractData data) {
        DatabaseReference databaseReference = null;
        if (data instanceof Animal) {
            databaseReference = database.getReference(BZFireBaseApi.animal);
        } else if (data instanceof Species) {
            databaseReference = database.getReference(BZFireBaseApi.animal_species);
        } else if (data instanceof Category) {
            databaseReference = database.getReference(BZFireBaseApi.animal_categories);
        }
        if (databaseReference != null) {
            //ToDo: implement remove image and child's parentUID
            databaseReference.child(data.getId()).removeValue();
        } else {
            if (getView() != null) {
                getView().onRemoveError("DatabaseReference NULL");
            }
        }
    }
}
