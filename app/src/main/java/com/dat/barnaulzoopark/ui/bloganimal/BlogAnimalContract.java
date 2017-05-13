package com.dat.barnaulzoopark.ui.bloganimal;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/13/2017.
 */

interface BlogAnimalContract {
    interface View extends MvpView {

    }

    interface UserActionListener extends MvpPresenter<View> {
    }
}
