package com.dat.barnaulzoopark.ui.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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
    public void updateProfilePicture(Uri uri) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
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
