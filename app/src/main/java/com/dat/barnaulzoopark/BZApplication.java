package com.dat.barnaulzoopark;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DAT on 03-May-16.
 */
public class BZApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //FireBase offline capabilities
        Fresco.initialize(this);
        MaterialViewPagerAnimator.ENABLE_LOG = false;
    }
}
