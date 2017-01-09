package com.dat.barnaulzoopark.ui.newsdetail;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/9/2017.
 */

interface NewsDetailContract {
    interface View extends MvpView {
        void showNews();
    }

    interface UserActionListener extends MvpPresenter<NewsDetailContract.View> {
    }
}
