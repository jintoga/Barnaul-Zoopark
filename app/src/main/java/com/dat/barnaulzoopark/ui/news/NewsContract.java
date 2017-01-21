package com.dat.barnaulzoopark.ui.news;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/7/2017.
 */

public interface NewsContract {
    interface View extends MvpView {
        void showAdminPrivilege(boolean isAdmin);
    }

    interface UserActionListener extends MvpPresenter<View> {
        void checkAdminPrivilege();
    }
}
