package com.dat.barnaulzoopark.ui.bloganimaleditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.adapters.MultiFileAttachmentAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 5/13/2017.
 */

public class BlogAnimalEditorActivity extends
    BaseMvpPhotoEditActivity<BlogAnimalEditorContract.View, BlogAnimalEditorContract.UserActionListener>
    implements BlogAnimalEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_BLOG_UID = "EXTRA_SELECTED_BLOG_UID";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    public static void start(@NonNull Context context, @Nullable String blogUid) {
        if (context instanceof BlogAnimalEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, BlogAnimalEditorActivity.class);
        if (blogUid != null) {
            intent.putExtra(EXTRA_SELECTED_BLOG_UID, blogUid);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_animal_editor);
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
    public BlogAnimalEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new BlogAnimalEditorPresenter(database, storage);
    }

    @Override
    public void onRemoved(int position) {

    }

    @Override
    public void onSlotSelected(int position) {

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
