package com.dat.barnaulzoopark.ui.startup.signup;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/3/2017.
 */

class SignUpPresenter extends MvpBasePresenter<SignUpContract.View>
    implements SignUpContract.UserActionListener {

    private static final String TAG = SignUpPresenter.class.getName();

    private FirebaseAuth auth;

    SignUpPresenter(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void signUpClicked(String email, String password) {
        Log.d(TAG, "signUpClicked");
        if (!"".equals(email) && !"".equals(password)) {
            if (isViewAttached() && getView() != null) {
                getView().showSigningUpProgress();
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "task");
                        if (!task.isSuccessful()) {
                            getView().showSignUpError(task.getException().getLocalizedMessage());
                        } else {
                            getView().showSignUpSuccess();
                        }
                    }
                });
        } else {
            if (isViewAttached() && getView() != null) {
                getView().showSignUpError("Email or password is empty.");
            }
        }
    }
}
