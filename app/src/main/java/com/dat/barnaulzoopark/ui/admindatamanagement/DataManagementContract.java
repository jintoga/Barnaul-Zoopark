package com.dat.barnaulzoopark.ui.admindatamanagement;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/28/2017.
 */

public interface DataManagementContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<DataManagementContract.View> {

    }
}
