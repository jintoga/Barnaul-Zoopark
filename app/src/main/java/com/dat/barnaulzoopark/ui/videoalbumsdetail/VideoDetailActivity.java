package com.dat.barnaulzoopark.ui.videoalbumsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;
import com.dat.barnaulzoopark.ui.YoutubeVideoFragment;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoDetailActivity extends BaseActivity {
    private static final String EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID";
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    public static void start(Activity activity, String videoId) {
        Intent intent = new Intent(activity, VideoDetailActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        init();
    }

    private void init() {
        String videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        if (videoId != null) {
            YoutubeVideoFragment youtubeVideoFragment = YoutubeVideoFragment.newInstance(videoId);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.youtubeContainer, youtubeVideoFragment)
                .commitAllowingStateLoss();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition(true);
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
}
