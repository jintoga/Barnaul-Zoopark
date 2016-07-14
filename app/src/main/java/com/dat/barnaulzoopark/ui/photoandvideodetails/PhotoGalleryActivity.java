package com.dat.barnaulzoopark.ui.photoandvideodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.PhotoAlbum;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoGalleryActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private static final String KEY_PHOTO_ALBUM = "PHOTO_ALBUM";
    private static final String KEY_PHOTO_ALBUM_NAME = "PHOTO_ALBUM_NAME";

    public static void startActivity(Context context, PhotoAlbum albumId) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(KEY_PHOTO_ALBUM, albumId.getId());
        intent.putExtra(KEY_PHOTO_ALBUM_NAME, albumId.getName());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            String albumId = getIntent().getStringExtra(KEY_PHOTO_ALBUM);
            String albumName = getIntent().getStringExtra(KEY_PHOTO_ALBUM_NAME);
            getSupportActionBar().setTitle(albumName);
            PhotoGalleryFragment fragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPhotoGallery);
            fragment.loadData(albumId);
        }

    }
}
