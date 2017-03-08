package com.dat.barnaulzoopark.ui.startup.login;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 1/4/2017.
 */

public interface LoginContract {
    interface View extends MvpView {
        void showLoginError(@NonNull String error);

        void showLoginSuccess();

        void showLoginProgress();
    }

    interface UserActionListener extends MvpPresenter<LoginContract.View> {
        void loginClicked(String email, String password);
    }
}
