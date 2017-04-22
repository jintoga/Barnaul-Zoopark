package com.dat.barnaulzoopark.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.startup.login.LoginFragment;
import com.dat.barnaulzoopark.ui.startup.signup.SignUpFragment;

public class StartupActivity extends AppCompatActivity implements ICallback {

    public static final int SKIP_POS = 0;
    public static final int LOGIN_POS = 1;
    public static final int SIGNUP_POS = 2;

    private static final String TAG = StartupActivity.class.getName();

    public static void start(Context context) {
        if (context instanceof StartupActivity) {
            return;
        }
        Intent intent = new Intent(context, StartupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        init();
    }

    private void init() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, new StartupFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void selected(int position) {
        Log.d(TAG, "selected " + position);
        changeFragment(position);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void onSignUpSuccess() {
        Log.d(TAG, "onSignUpSuccess");
        saveLoggedInStatus();
    }

    @Override
    public void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess");
        saveLoggedInStatus();
    }

    private void saveLoggedInStatus() {
        Log.d(TAG, "saveLoggedInStatus");
        BZApplication.get(this).getApplicationComponent().preferencesHelper().setIsLoggedIn(true);
        goToMain();
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
                fragment = new SignUpFragment();
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
        finish();
        MainActivity.start(this);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
