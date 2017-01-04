package com.dat.barnaulzoopark.ui.startup.login;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/4/2017.
 */

public class LoginPresenter extends MvpBasePresenter<LoginContract.View>
    implements LoginContract.UserActionListener {

    private static final String TAG = LoginPresenter.class.getName();
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    LoginPresenter(FirebaseAuth auth, FirebaseDatabase database) {
        this.auth = auth;
        this.database = database;
    }

    @Override
    public void loginClicked(String email, String password) {
        Log.d(TAG, "loginClicked");
        if (!"".equals(email) && !"".equals(password)) {
            if (getView() != null) {
                getView().showLoginProgress();
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUserExist(authResult.getUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getView().showLoginError(e.getLocalizedMessage());
                    }
                });
        } else {
            if (getView() != null) {
                getView().showLoginError("Email or password is empty.");
            }
        }
    }

    private void checkUserExist(@NonNull final String userUID) {
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userUID)) {
                    if (getView() != null) {
                        getView().showLoginSuccess();
                    }
                } else {
                    if (getView() != null) {
                        getView().showLoginError("This user doesn't exist.");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
