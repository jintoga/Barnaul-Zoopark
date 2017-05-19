package com.dat.barnaulzoopark;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import com.dat.barnaulzoopark.model.User;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import okhttp3.OkHttpClient;

/**
 * Created by DAT on 03-May-16.
 */
public class BZApplication extends MultiDexApplication {

    @Inject
    OkHttpClient okHttpClient;

    @NonNull
    private ApplicationComponent applicationComponent;

    public static BZApplication get(Context context) {
        return (BZApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
        applicationComponent.inject(this);
        applicationComponent.fireBaseDatabase()
            .setPersistenceEnabled(true); //FireBase offline capabilities

        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        ImagePipelineConfig config =
            OkHttpImagePipelineConfigFactory.newBuilder(getApplicationContext(), okHttpClient)
                .setDownsampleEnabled(true)
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(getApplicationContext(), config);
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);

        MaterialViewPagerAnimator.ENABLE_LOG = false;
    }

    @Override
    public void onLowMemory() {
        Fresco.getImagePipeline().clearMemoryCaches();
        super.onLowMemory();
    }

    @NonNull
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public boolean isTabletLandscape() {
        return applicationComponent.context().getResources().getBoolean(R.bool.isTablet)
            && applicationComponent.context().getResources().getConfiguration().orientation
            == Configuration.ORIENTATION_LANDSCAPE;
    }

    public boolean isAdmin() {
        return applicationComponent.preferencesHelper().isAdmin();
    }

    @Nullable
    public User getUser() {
        return applicationComponent.preferencesHelper().getUser();
    }
}
