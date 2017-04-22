package com.dat.barnaulzoopark.ui.newsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;

/**
 * Created by DAT on 1/9/2017.
 */

public class NewsDetailActivity extends BaseActivity {

    private static final String KEY_NEWS_UID = "NEWS_UID";

    public static void startActivity(Activity activity, @NonNull String uid) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(KEY_NEWS_UID, uid);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        String newsUid = getIntent().getStringExtra(KEY_NEWS_UID);
        if (savedInstanceState == null && newsUid != null) {
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(newsUid);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, newsDetailFragment, "")
                .commit();
        }
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
    protected void onStart() {
        super.onStart();
        if (BZApplication.get(this).isTabletLandscape()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition(true);
    }
}
