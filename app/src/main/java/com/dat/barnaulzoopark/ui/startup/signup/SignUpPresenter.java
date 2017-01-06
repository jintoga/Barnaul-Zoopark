package com.dat.barnaulzoopark.ui.startup.signup;

import android.support.annotation.NonNull;
import android.util.Log;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/3/2017.
 */

class SignUpPresenter extends MvpBasePresenter<SignUpContract.View>
    implements SignUpContract.UserActionListener {

    private static final String TAG = SignUpPresenter.class.getName();

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    SignUpPresenter(FirebaseAuth auth, FirebaseDatabase database) {
        this.auth = auth;
        this.database = database;
    }

    @Override
    public void signUpClicked(final String userName, final String email, final String password) {
        Log.d(TAG, "signUpClicked");
        if (!"".equals(email) && !"".equals(password)) {
            if (getView() != null) {
                getView().showSigningUpProgress();
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserInfoToDatabase(authResult.getUser().getUid(), userName, email);
                        if (getView() != null) {
                            getView().showSignUpSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getView().showSignUpError(e.getLocalizedMessage());
                    }
                });
        } else {
            if (getView() != null) {
                getView().showSignUpError("Email or password is empty.");
            }
        }
    }

    private void addUserInfoToDatabase(@NonNull String userUID, @NonNull String name,
        @NonNull String email) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.users);
        DatabaseReference currentUserReference = databaseReference.child(userUID);
        currentUserReference.child("name").setValue(name);
        currentUserReference.child("email").setValue(email);
    }
}
