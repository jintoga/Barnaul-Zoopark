package com.dat.barnaulzoopark.ui.photoalbumsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.ui.BaseActivity;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.google.gson.Gson;

public class PhotoAlbumsDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private static final String KEY_PHOTO_ALBUM = "PHOTO_ALBUM";

    public static void startActivity(Activity activity, PhotoAlbum photoAlbum) {
        Intent intent = new Intent(activity, PhotoAlbumsDetailActivity.class);
        String photoAlbumJson = new Gson().toJson(photoAlbum);
        intent.putExtra(KEY_PHOTO_ALBUM, photoAlbumJson);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_albums_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String albumJson = getIntent().getStringExtra(KEY_PHOTO_ALBUM);
        if (albumJson == null) {
            finish();
            return;
        }
        PhotoAlbum photoAlbum = new Gson().fromJson(albumJson, PhotoAlbum.class);
        if (getSupportActionBar() != null) {
            String name = String.format("%s\n%s", photoAlbum.getName(),
                ConverterUtils.getConvertedTime(photoAlbum.getTime()));
            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        PhotoAlbumsDetailFragment fragment =
            (PhotoAlbumsDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragmentPhotoGallery);
        fragment.loadData(photoAlbum);
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
