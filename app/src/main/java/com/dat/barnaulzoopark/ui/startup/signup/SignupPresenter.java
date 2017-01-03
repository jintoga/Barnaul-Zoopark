package com.dat.barnaulzoopark.ui.startup.signup;

import android.util.Log;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/3/2017.
 */

public class SignupPresenter extends MvpBasePresenter<SignupContract.View>
    implements SignupContract.UserActionListener {

    private static final String TAG = SignupPresenter.class.getName();

    @Override
    public void signUpClicked() {
        Log.d(TAG, "signUpClicked");
        getView().showSignupError("ERr");
        getView().showSignupSuccess();
    }
}
