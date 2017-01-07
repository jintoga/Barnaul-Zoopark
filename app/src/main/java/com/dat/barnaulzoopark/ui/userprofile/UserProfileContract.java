package com.dat.barnaulzoopark.ui.userprofile;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.User;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/6/2017.
 */

public interface UserProfileContract {
    interface View extends MvpView {
        void bindUserData(@NonNull User user);

        void bindUserDataAsGuest();

        void showUpdateProfileError(@NonNull String error);

        void showUpdateProfileProgress();

        void showUpdateProfileSuccess();
    }

    interface UserActionListener extends MvpPresenter<UserProfileContract.View> {
        void loadUserData();

        void updateProfilePicture(Uri uri);

        void browseProfilePicture(Activity activity, int request);

        void updateUserName(String s);
    }
}
