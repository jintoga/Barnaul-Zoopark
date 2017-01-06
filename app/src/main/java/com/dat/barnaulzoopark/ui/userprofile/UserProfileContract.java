package com.dat.barnaulzoopark.ui.userprofile;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/6/2017.
 */

public interface UserProfileContract {
    interface View extends MvpView {
        void showUpdateProfileError(@NonNull String error);

        void showUpdateProfileSuccess();
    }

    interface UserActionListener extends MvpPresenter<UserProfileContract.View> {
        void updateProfilePicture(Uri uri);

        void browseProfilePicture(Activity activity, int request);
    }
}
