package com.dat.barnaulzoopark.ui.videoalbumsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.dat.barnaulzoopark.ui.BaseActivity;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.google.gson.Gson;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbumsDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private static final String EXTRA_VIDEO_ALBUM = "EXTRA_VIDEO_ALBUM";

    public static void startActivity(Activity activity, @NonNull VideoAlbum videoAlbum) {
        if (activity instanceof VideoAlbumsDetailActivity) {
            return;
        }
        Intent intent = new Intent(activity, VideoAlbumsDetailActivity.class);
        String photoAlbumJson = new Gson().toJson(videoAlbum);
        intent.putExtra(EXTRA_VIDEO_ALBUM, photoAlbumJson);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_albums_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String albumJson = getIntent().getStringExtra(EXTRA_VIDEO_ALBUM);
        if (albumJson == null) {
            finish();
            return;
        }
        VideoAlbum videoAlbum = new Gson().fromJson(albumJson, VideoAlbum.class);
        if (getSupportActionBar() != null) {
            String name = String.format("%s\n%s", videoAlbum.getName(),
                ConverterUtils.getConvertedTime(videoAlbum.getTime()));
            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        VideoAlbumsDetailFragment fragment =
            (VideoAlbumsDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragmentVideoAlbum);
        fragment.loadData(videoAlbum);
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
