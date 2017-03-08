package com.dat.barnaulzoopark.ui;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/6/2017.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends MvpActivity<V, P> {
}
