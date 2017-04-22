package com.dat.barnaulzoopark;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 2/26/2017.
 */

@Module
public class ApplicationModule {
    private BZApplication application;

    public ApplicationModule(BZApplication application) {
        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public BZApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    PreferenceHelper providePreferenceHelper() {
        return new PreferenceHelper(application.getApplicationComponent().context());
    }
}
