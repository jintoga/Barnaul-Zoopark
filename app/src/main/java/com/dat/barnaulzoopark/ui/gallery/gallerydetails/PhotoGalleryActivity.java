package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;

public class PhotoGalleryActivity extends AppCompatActivity {

    private static final String KEY_PHOTO_ALBUM = "PHOTO_ALBUM";

    public static void startActivity(Context context, PhotoAlbum photoAlbum) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(KEY_PHOTO_ALBUM, photoAlbum);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);
    }
}
