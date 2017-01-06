package com.dat.barnaulzoopark.ui.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/6/2017.
 */

public class UserProfilePresenter extends MvpBasePresenter<UserProfileContract.View>
    implements UserProfileContract.UserActionListener {

    private static final String TAG = UserProfilePresenter.class.getName();
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public UserProfilePresenter(FirebaseAuth auth, FirebaseDatabase database) {
        this.auth = auth;
        this.database = database;
    }

    @Override
    public void loadUserData() {
        if (auth.getCurrentUser() != null) {
            DatabaseReference currentUser = database.getReference()
                .child(BZFireBaseApi.users)
                .child(auth.getCurrentUser().getUid());
            currentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = null;
                    String photoUrl = null;
                    String email = null;
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        if (item.getKey().equals("name")) {
                            name = (String) item.getValue();
                        }
                        if (item.getKey().equals("email")) {
                            email = (String) item.getValue();
                        }
                        if (item.getKey().equals("photo")) {
                            photoUrl = (String) item.getValue();
                        }
                    }
                    if (getView() != null) {
                        getView().bindUserData(name, email, photoUrl);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            if (getView() != null) {
                getView().bindUserDataAsGuest();
            }
        }
    }

    @Override
    public void updateProfilePicture(Uri uri) {
        if (auth.getCurrentUser() != null) {
            //ToDo: update user profile with FireBase Database and Storage
        }
    }

    @Override
    public void browseProfilePicture(Activity activity, int request) {
        Log.d(TAG, "browsing image");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/+");
        activity.startActivityForResult(intent, request);
    }
}
