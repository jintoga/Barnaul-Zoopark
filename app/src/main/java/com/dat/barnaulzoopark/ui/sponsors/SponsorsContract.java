package com.dat.barnaulzoopark.ui.sponsors;

import com.google.firebase.database.DatabaseReference;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 6/4/2017.
 */

interface SponsorsContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<View> {
        DatabaseReference getSponsorsReference();
    }
}
