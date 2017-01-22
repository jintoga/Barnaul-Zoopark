package com.dat.barnaulzoopark.ui.newsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.News;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/9/2017.
 */

interface NewsDetailContract {
    interface View extends MvpView {
        void showNews(@NonNull News news);
    }

    interface UserActionListener extends MvpPresenter<NewsDetailContract.View> {
        void loadNewsDetail(@NonNull String newsUid);
    }
}
