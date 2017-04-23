package com.dat.barnaulzoopark.ui.news;

import com.google.firebase.database.DatabaseReference;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/7/2017.
 */

public interface NewsContract {
    interface View extends MvpView {
        void bindNewsDetail();
    }

    interface UserActionListener extends MvpPresenter<View> {
        DatabaseReference getNewsReference();

        void loadData();
    }
}
