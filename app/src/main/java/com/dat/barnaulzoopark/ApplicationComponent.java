package com.dat.barnaulzoopark;

import android.content.Context;
import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.FirebaseModule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import dagger.Component;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 2/26/2017.
 */
@Singleton
@Component(modules = {
    ApplicationModule.class, FirebaseModule.class
})
public interface ApplicationComponent {
    @NonNull
    BZApplication application();

    @NonNull
    FirebaseDatabase fireBaseDatabase();

    @NonNull
    FirebaseStorage fireBaseStorage();

    @NonNull
    FirebaseAuth firebaseAuth();

    @NonNull
    EventBus eventBus();

    void inject(BZApplication application);

    @NonNull
    PreferenceHelper preferencesHelper();

    Context context();
}
