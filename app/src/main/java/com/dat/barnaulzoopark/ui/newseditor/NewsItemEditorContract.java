package com.dat.barnaulzoopark.ui.newseditor;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/29/2017.
 */

public interface NewsItemEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<NewsItemEditorContract.View> {
    }
}
