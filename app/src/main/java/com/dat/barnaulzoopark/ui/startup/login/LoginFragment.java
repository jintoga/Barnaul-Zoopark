package com.dat.barnaulzoopark.ui.startup.login;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.PasswordView;
import com.dat.barnaulzoopark.ui.startup.ICallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DAT on 20-Mar-16.
 */
public class LoginFragment extends Fragment {

    @Bind(R.id.email)
    protected EditText email;
    @Bind(R.id.password)
    protected PasswordView password;
    private View view;
    private ICallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        email.requestFocus();
        email.setTypeface(Typeface.MONOSPACE);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ICallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @OnClick(R.id.back)
    protected void back() {
        callback.back();
    }
}
