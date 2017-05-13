package com.dat.barnaulzoopark.ui.bloganimal;

import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.ui.EventMvpPresenter;
import com.google.firebase.database.FirebaseDatabase;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 5/13/2017.
 */

class BlogAnimalPresenter extends EventMvpPresenter<BlogAnimalContract.View>
    implements BlogAnimalContract.UserActionListener {

    private FirebaseDatabase database;

    BlogAnimalPresenter(@Nullable EventBus eventBus, FirebaseDatabase database) {
        super(eventBus);
        this.database = database;
    }
}
