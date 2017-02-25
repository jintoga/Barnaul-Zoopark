package com.dat.barnaulzoopark.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by DAT on 2/26/2017.
 */

@Module
public class FirebaseModule {

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
}
