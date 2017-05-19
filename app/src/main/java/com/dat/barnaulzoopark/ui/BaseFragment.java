package com.dat.barnaulzoopark.ui;

import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by Nguyen on 6/10/2016.
 */
//In future this class should extend BaseMvpFragment
public class BaseFragment extends Fragment {
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
