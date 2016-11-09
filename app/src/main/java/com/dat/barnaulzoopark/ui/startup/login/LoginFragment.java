package com.dat.barnaulzoopark.ui.startup.login;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.widget.PasswordView;
import com.dat.barnaulzoopark.ui.startup.ICallback;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 20-Mar-16.
 */
public class LoginFragment extends Fragment {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.email)
    protected EditText email;
    @Bind(R.id.password)
    protected PasswordView password;
    private View view;
    private ICallback callback;

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

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
            if (toolbar.getNavigationIcon() != null) {
                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
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
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.login, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
