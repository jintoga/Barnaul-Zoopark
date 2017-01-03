package com.dat.barnaulzoopark.ui.startup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 20-Mar-16.
 */
public class StartupFragment extends Fragment {

    private ICallback callback;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_startup, container, false);
        ButterKnife.bind(this, view);
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

    @OnClick(R.id.skip)
    protected void skip() {
        callback.selected(StartupActivity.SKIP_POS);
    }

    @OnClick(R.id.goToSignup)
    protected void signUp() {
        callback.selected(StartupActivity.SIGNUP_POS);
    }

    @OnClick(R.id.goToLogin)
    protected void login() {
        callback.selected(StartupActivity.LOGIN_POS);
    }

    @OnClick(R.id.loginFb)
    protected void loginFb() {
        Log.d("loginFb", "loginFb");
    }

    @OnClick(R.id.loginVk)
    protected void loginVk() {
        Log.d("loginVk", "loginVk");
    }
}
