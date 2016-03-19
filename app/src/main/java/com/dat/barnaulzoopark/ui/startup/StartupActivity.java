package com.dat.barnaulzoopark.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dat.barnaulzoopark.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartupActivity extends AppCompatActivity {

    @Bind(R.id.skip)
    protected View skip;
    @Bind(R.id.signup)
    protected View signUp;
    @Bind(R.id.login)
    protected View login;
    @Bind(R.id.loginFb)
    protected View loginFb;
    @Bind(R.id.loginVk)
    protected View loginVk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.skip)
    protected void skip() {
        Log.d("skip", "skip");
    }

    @OnClick(R.id.signup)
    protected void signUp() {
        Log.d("signUp", "signUp");
    }

    @OnClick(R.id.login)
    protected void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


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
