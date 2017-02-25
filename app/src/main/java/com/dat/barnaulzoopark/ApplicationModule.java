package com.dat.barnaulzoopark;

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
    @Singleton
    public BZApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
