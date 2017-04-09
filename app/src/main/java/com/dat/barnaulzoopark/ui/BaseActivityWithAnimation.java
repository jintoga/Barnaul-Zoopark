package com.dat.barnaulzoopark.ui;

import com.dat.barnaulzoopark.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 4/9/2017.
 */

public abstract class BaseActivityWithAnimation<V extends MvpView, P extends MvpPresenter<V>>
    extends MvpActivity<V, P> {
    public void finishWithTransition(boolean withAnimation) {
        finish();
        if (withAnimation) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
    }
}
