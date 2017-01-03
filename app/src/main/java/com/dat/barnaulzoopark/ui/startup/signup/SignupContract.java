package com.dat.barnaulzoopark.ui.startup.signup;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/3/2017.
 */

public interface SignUpContract {
    interface View extends MvpView {
        void showSignUpError(@NonNull String error);

        void showSignUpSuccess();

        void showSigningUpProgress();
    }

    interface UserActionListener extends MvpPresenter<View> {
        void signUpClicked(String email, String password);
    }
}
