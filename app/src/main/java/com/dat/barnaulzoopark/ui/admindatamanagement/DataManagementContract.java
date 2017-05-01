package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.AbstractData;
import com.google.firebase.database.DatabaseReference;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/28/2017.
 */

public interface DataManagementContract {
    interface View extends MvpView {
        void onRemoveError(@NonNull String errorMsg);
    }

    interface UserActionListener extends MvpPresenter<DataManagementContract.View> {
        DatabaseReference getDataReference(String referenceName);

        void removeItem(AbstractData data);
    }
}
