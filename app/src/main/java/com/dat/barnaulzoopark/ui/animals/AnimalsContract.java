package com.dat.barnaulzoopark.ui.animals;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 3/28/2017.
 */

public interface AnimalsContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<AnimalsContract.View> {
    }
}
