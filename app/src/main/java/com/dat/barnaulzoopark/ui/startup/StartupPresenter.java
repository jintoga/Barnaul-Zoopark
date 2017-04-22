package com.dat.barnaulzoopark.ui.startup;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.ui.EventMvpPresenter;
import com.dat.barnaulzoopark.ui.userprofile.UserProfilePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 4/23/2017.
 */

public class StartupPresenter extends EventMvpPresenter<StartupContract.View>
    implements StartupContract.UserActionListener {
    private static final String TAG = UserProfilePresenter.class.getName();

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public StartupPresenter(EventBus eventBus, FirebaseAuth auth, FirebaseDatabase database) {
        super(eventBus);
        this.auth = auth;
        this.database = database;
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
                            getView().bindAdminPrivilege(true);
                        }
                    } else {
                        if (getView() != null) {
                            getView().bindAdminPrivilege(false);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().bindAdminPrivilege(false);
                    }
                }
            });
        } else {
            if (getView() != null) {
                getView().bindAdminPrivilege(false);
            }
        }
    }
}
