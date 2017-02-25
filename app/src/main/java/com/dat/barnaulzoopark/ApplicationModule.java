package com.dat.barnaulzoopark;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
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
    public FirebaseDatabase provideFireBaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseStorage provideFireBaseStorage() {
        return FirebaseStorage.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFireBaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
