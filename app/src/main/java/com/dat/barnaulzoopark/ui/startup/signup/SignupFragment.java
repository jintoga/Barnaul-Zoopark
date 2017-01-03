package com.dat.barnaulzoopark.ui.startup.signup;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.widget.PasswordView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by DAT on 20-Mar-16.
 */
public class SignUpFragment
    extends BaseMvpFragment<SignUpContract.View, SignUpContract.UserActionListener>
    implements SignUpContract.View {

    private static final String TAG = SignUpFragment.class.getName();
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.email)
    protected EditText email;
    @Bind(R.id.password)
    protected PasswordView password;
    private View view;

    @Override
    public SignUpContract.UserActionListener createPresenter() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return new SignUpPresenter(getContext(), auth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        email.requestFocus();
        email.setTypeface(Typeface.MONOSPACE);
        return view;
    }

    @Override
    public void showSignupError(@NonNull String error) {
        Log.d(TAG, "showSignupError: " + error);
    }

    @Override
    public void showSignupSuccess() {
        Log.d(TAG, "showSignupSuccess");
    }

    @Override
    public void showSigningUpProgress() {
        Log.d(TAG, "showSigningUpProgress");
    }

    @Override
    public void moveForward() {
        Log.d(TAG, "moveForward");
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
            if (toolbar.getNavigationIcon() != null) {
                toolbar.getNavigationIcon()
                    .setColorFilter(getResources().getColor(R.color.white),
                        PorterDuff.Mode.SRC_ATOP);
            }
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }

    @OnClick(R.id.signUp)
    protected void signUpClicked() {
        Log.d(TAG, "signUpClicked");
        getPresenter().signUpClicked(email.getText().toString(), password.getPassword());
    }
}
