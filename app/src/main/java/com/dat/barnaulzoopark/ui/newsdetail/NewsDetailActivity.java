package com.dat.barnaulzoopark.ui.newsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;

/**
 * Created by DAT on 1/9/2017.
 */

public class NewsDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    public static void startActivity(Activity activity, @NonNull String uid) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        CollapsingToolbarLayout.LayoutParams layoutParams =
            (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishWithTransition(true);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition(true);
    }
}
