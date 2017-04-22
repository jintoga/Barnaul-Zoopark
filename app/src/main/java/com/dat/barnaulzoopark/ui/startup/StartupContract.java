package com.dat.barnaulzoopark.ui.startup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/23/2017.
 */

public interface StartupContract {
    interface View extends MvpView {
        void bindAdminPrivilege(boolean isAdmin);
    }

    interface UserActionListener extends MvpPresenter<StartupContract.View> {
        void checkAdminPrivilege();
    }
}
