package com.dat.barnaulzoopark.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.startup.login.LoginFragment;
import com.dat.barnaulzoopark.ui.startup.signup.SignUpFragment;

public class StartupActivity extends BaseActivity implements ICallback {

    public static final int SKIP_POS = 0;
    public static final int LOGIN_POS = 1;
    public static final int SIGNUP_POS = 2;

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
        changeFragment(position);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void onSignUpSuccess() {
        goToMain();
    }

    @Override
    public void onLoginSuccess() {
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
