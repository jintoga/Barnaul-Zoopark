package com.dat.barnaulzoopark.ui;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 28-Apr-16.
 */
public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> {
}