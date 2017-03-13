package com.dat.barnaulzoopark.ui.animaleditor;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 3/11/2017.
 */

public interface AnimalEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<AnimalEditorContract.View> {

    }
}
