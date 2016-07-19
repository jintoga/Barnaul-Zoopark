package com.dat.barnaulzoopark.ui.photoalbumsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.ui.BaseActivity;

public class PhotoAlbumsDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private static final String KEY_PHOTO_ALBUM = "PHOTO_ALBUM";
    private static final String KEY_PHOTO_ALBUM_NAME = "PHOTO_ALBUM_NAME";

    public static void startActivity(Activity activity, PhotoAlbum albumId) {
        Intent intent = new Intent(activity, PhotoAlbumsDetailActivity.class);
        intent.putExtra(KEY_PHOTO_ALBUM, albumId.getId());
        intent.putExtra(KEY_PHOTO_ALBUM_NAME, albumId.getName());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_albums_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String albumId = getIntent().getStringExtra(KEY_PHOTO_ALBUM);
        String albumName = getIntent().getStringExtra(KEY_PHOTO_ALBUM_NAME);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(albumName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        PhotoAlbumsDetailFragment fragment =
            (PhotoAlbumsDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragmentPhotoGallery);
        fragment.loadData(albumId);
    }
}
