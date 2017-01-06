package com.dat.barnaulzoopark.ui.startup.signup;

import android.content.Context;
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
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.startup.ICallback;
import com.dat.barnaulzoopark.widget.PasswordView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DAT on 20-Mar-16.
 */
public class SignUpFragment
    extends BaseMvpFragment<SignUpContract.View, SignUpContract.UserActionListener>
    implements SignUpContract.View {

    private static final String TAG = SignUpFragment.class.getName();
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.userName)
    protected EditText userName;
    @Bind(R.id.email)
    protected EditText email;
    @Bind(R.id.password)
    protected PasswordView password;
    private View view;
    private ICallback callback;

    private MaterialDialog progressDialog;

    @Override
    public SignUpContract.UserActionListener createPresenter() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return new SignUpPresenter(auth, database);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        userName.requestFocus();
        userName.setTypeface(Typeface.MONOSPACE);
        email.setTypeface(Typeface.MONOSPACE);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ICallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void showSignUpError(@NonNull String error) {
        Log.d(TAG, "showSignUpError: " + error);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        BZDialogBuilder.createSimpleErrorDialog(getContext(), error);
    }

    @Override
    public void showSignUpSuccess() {
        Log.d(TAG, "showSignUpSuccess");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        callback.onSignUpSuccess();
    }

    @Override
    public void showSigningUpProgress() {
        Log.d(TAG, "showSigningUpProgress");
        progressDialog = BZDialogBuilder.createSimpleProgressDialog(getContext());
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
        getPresenter().signUpClicked(userName.getText().toString(), email.getText().toString(),
            password.getPassword());
    }
}
