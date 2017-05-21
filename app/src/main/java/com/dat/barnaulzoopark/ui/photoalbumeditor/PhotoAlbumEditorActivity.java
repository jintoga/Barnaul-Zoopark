package com.dat.barnaulzoopark.ui.photoalbumeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 5/21/2017.
 */

public class PhotoAlbumEditorActivity extends
    BaseMvpPhotoEditActivity<PhotoAlbumEditorContract.View, PhotoAlbumEditorContract.UserActionListener>
    implements PhotoAlbumEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_PHOTO_ALBUM_UID = "EXTRA_SELECTED_PHOTO_ALBUM_UID";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.photoAlbumEditorContent)
    protected RecyclerView photoAlbumEditorContent;

    private PhotoAlbum selectedPhotoAlbum;

    private MaterialDialog progressDialog;

    public static void start(Context context, @Nullable String photoAlbumUid) {
        if (context instanceof PhotoAlbumEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, PhotoAlbumEditorActivity.class);
        if (photoAlbumUid != null) {
            intent.putExtra(EXTRA_SELECTED_PHOTO_ALBUM_UID, photoAlbumUid);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album_editor);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        init();
        setPhotoEditListener(this);
    }

    private void init() {

    }

    @NonNull
    @Override
    public PhotoAlbumEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new PhotoAlbumEditorPresenter(database, storage);
    }

    @Override
    public void onRemovedPhotoClicked(int requestCode) {

    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {

    }

    @Override
    public void onCropError(@NonNull String errorMsg) {

    }
}
