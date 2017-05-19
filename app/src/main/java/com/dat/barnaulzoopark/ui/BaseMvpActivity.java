package com.dat.barnaulzoopark.ui;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import com.dat.barnaulzoopark.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/6/2017.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends MvpActivity<V, P> {
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
