package com.dat.barnaulzoopark.ui.news;

import android.util.Log;
import com.dat.barnaulzoopark.events.LoggedIn;
import com.dat.barnaulzoopark.ui.EventMvpPresenter;
import com.dat.barnaulzoopark.ui.userprofile.UserProfilePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by DAT on 1/7/2017.
 */

class NewPresenter extends EventMvpPresenter<NewsContract.View>
    implements NewsContract.UserActionListener {

    private static final String TAG = UserProfilePresenter.class.getName();

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    NewPresenter(EventBus eventBus, FirebaseAuth auth, FirebaseDatabase database,
        FirebaseStorage storage) {
        super(eventBus);
        this.auth = auth;
        this.database = database;
        this.storage = storage;
    }

    @Subscribe
    public void onEvent(LoggedIn loggedIn) {
        Log.d(TAG, "is admin: " + loggedIn.isAdmin());
        if (getView() != null) {
            getView().showAdminPrivilege(loggedIn.isAdmin());
        }
    }
}
