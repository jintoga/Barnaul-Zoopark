package com.dat.barnaulzoopark.ui.startup.login;

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
import android.view.Menu;
import android.view.MenuInflater;
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
public class LoginFragment
    extends BaseMvpFragment<LoginContract.View, LoginContract.UserActionListener>
    implements LoginContract.View {

    private static final String TAG = LoginFragment.class.getName();

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.email)
    protected EditText email;
    @Bind(R.id.password)
    protected PasswordView password;
    private View view;
    private ICallback callback;

    private MaterialDialog progressDialog;

    @Override
    public LoginContract.UserActionListener createPresenter() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return new LoginPresenter(auth, database);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        initToolbar();
        email.requestFocus();
        email.setTypeface(Typeface.MONOSPACE);
        return view;
    }

    @Override
    public void showLoginError(@NonNull String error) {
        Log.d(TAG, "showLoginError: " + error);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        BZDialogBuilder.createSimpleErrorDialog(getContext(), error);
    }

    @Override
    public void showLoginSuccess() {
        Log.d(TAG, "showLoginProgress");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        callback.onLoginSuccess();
    }

    @Override
    public void showLoginProgress() {
        Log.d(TAG, "showLoginProgress");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.login, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.login)
    protected void loginClicked() {
        Log.d(TAG, "loginClicked");
        getPresenter().loginClicked(email.getText().toString(), password.getPassword());
    }
}
