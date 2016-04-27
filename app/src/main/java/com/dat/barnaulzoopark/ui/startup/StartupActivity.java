package com.dat.barnaulzoopark.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.startup.login.LoginFragment;
import com.dat.barnaulzoopark.ui.startup.signup.SignupFragment;

public class StartupActivity extends BaseActivity implements ICallback {

    public static final int SKIP_POS = 0;
    public static final int LOGIN_POS = 1;
    public static final int SIGNUP_POS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        init();
    }

    private void init() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, new StartupFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void selected(int position) {
        changeFragment(position);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    private void changeFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (position) {
            case SKIP_POS:
                goToMain();
                break;
            case LOGIN_POS:
                fragment = new LoginFragment();
                break;
            case SIGNUP_POS:
                fragment = new SignupFragment();
                break;
            default:
                break;
        }
        if (fragment != null) {
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
