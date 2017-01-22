package com.dat.barnaulzoopark;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DAT on 03-May-16.
 */
public class BZApplication extends Application {

    public static final String BZSharedPreference = "BZSharedPreference";
    public static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //FireBase offline capabilities
        Fresco.initialize(this);
        MaterialViewPagerAnimator.ENABLE_LOG = false;
    }

    public static boolean isTabletLandscape(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet)
            && context.getResources().getConfiguration().orientation
            == Configuration.ORIENTATION_LANDSCAPE;
    }
}
