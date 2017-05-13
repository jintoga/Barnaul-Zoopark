package com.dat.barnaulzoopark.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
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

    protected void showSnackBar(@NonNull String msg) {
        if (findViewById(R.id.container) != null) {
            Snackbar snackbar =
                Snackbar.make(findViewById(R.id.container), msg, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    protected void showToast(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
