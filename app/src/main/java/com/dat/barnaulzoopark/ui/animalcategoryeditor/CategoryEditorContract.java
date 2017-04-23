package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/23/2017.
 */

public interface CategoryEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<CategoryEditorContract.View> {

    }
}
