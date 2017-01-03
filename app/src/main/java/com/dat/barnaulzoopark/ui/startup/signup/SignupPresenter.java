package com.dat.barnaulzoopark.ui.startup.signup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/3/2017.
 */

public class SignUpPresenter extends MvpBasePresenter<SignUpContract.View>
    implements SignUpContract.UserActionListener {

    private static final String TAG = SignUpPresenter.class.getName();

    private Context context;
    private FirebaseAuth auth;

    public SignUpPresenter(Context context, FirebaseAuth auth) {
        this.context = context;
        this.auth = auth;
    }

    @Override
    public void signUpClicked(String email, String password) {
        Log.d(TAG, "signUpClicked");
        if (!"".equals(email) && !"".equals(password)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getView().showSignupError(e.getLocalizedMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        getView().showSignupSuccess();
                    }
                });
        } else {
            BZDialogBuilder.createSimpleErrorDialog(context, "Email or password is empty.");
        }
    }
}
