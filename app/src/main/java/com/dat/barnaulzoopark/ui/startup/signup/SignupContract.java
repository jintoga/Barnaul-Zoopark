package com.dat.barnaulzoopark.ui.startup.signup;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/3/2017.
 */

public interface SignupContract {
    interface View extends MvpView {
        void showSignupError(@NonNull String error);

        void showSignupSuccess();
    }

    interface UserActionListener extends MvpPresenter<View> {
        void signUpClicked();
    }
}
