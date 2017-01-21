package com.dat.barnaulzoopark.ui.news;

import android.util.Log;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.events.LoggedIn;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.ui.EventMvpPresenter;
import com.dat.barnaulzoopark.ui.userprofile.UserProfilePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by DAT on 1/7/2017.
 */

class NewsPresenter extends EventMvpPresenter<NewsContract.View>
    implements NewsContract.UserActionListener {

    private static final String TAG = UserProfilePresenter.class.getName();

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    NewsPresenter(EventBus eventBus, FirebaseAuth auth, FirebaseDatabase database,
        FirebaseStorage storage) {
        super(eventBus);
        this.auth = auth;
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void checkAdminPrivilege() {
        if (auth.getCurrentUser() != null) {
            DatabaseReference currentUserDatabaseRef = database.getReference()
                .child(BZFireBaseApi.users)
                .child(auth.getCurrentUser().getUid());
            currentUserDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.isAdmin()) {
                        if (getView() != null) {
                            getView().showAdminPrivilege(true);
                        }
                    } else {
                        if (getView() != null) {
                            getView().showAdminPrivilege(false);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().showAdminPrivilege(false);
                    }
                }
            });
        } else {
            if (getView() != null) {
                getView().showAdminPrivilege(false);
            }
        }
    }

    @Subscribe
    public void onEvent(LoggedIn loggedIn) {
        Log.d(TAG, "is admin: " + loggedIn.isAdmin());
        if (getView() != null) {
            getView().showAdminPrivilege(loggedIn.isAdmin());
        }
    }
}
