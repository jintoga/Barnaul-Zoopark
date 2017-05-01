package com.dat.barnaulzoopark.ui.animaleditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.adapters.MultiFileAttachmentAdapter;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;

/**
 * Created by DAT on 3/11/2017.
 */

public class AnimalEditorActivity extends
    BaseMvpPhotoEditActivity<AnimalEditorContract.View, AnimalEditorContract.UserActionListener>
    implements AnimalEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    public static void start(Context context) {
        if (context instanceof AnimalEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, AnimalEditorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_editor);
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
    public AnimalEditorContract.UserActionListener createPresenter() {
        return null;
    }

    @Override
    public void onRemoved(int position) {

    }

    @Override
    public void onSlotSelected(int position) {

    }

    @Override
    public void onRemovedPhotoClicked() {

    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {

    }

    @Override
    public void onCropError(@NonNull String errorMsg) {

    }
}
