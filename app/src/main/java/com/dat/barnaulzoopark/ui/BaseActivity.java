package com.dat.barnaulzoopark.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 28-Apr-16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void finishWithTransition(boolean withAnimation) {
        finish();
        if (withAnimation) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
