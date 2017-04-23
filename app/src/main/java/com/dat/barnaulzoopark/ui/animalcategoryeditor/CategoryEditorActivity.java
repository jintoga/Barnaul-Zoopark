package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorActivity extends
    BaseMvpPhotoEditActivity<CategoryEditorContract.View, CategoryEditorContract.UserActionListener>
    implements CategoryEditorContract.View, BaseMvpPhotoEditActivity.Listener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    public static void start(Context context) {
        if (context instanceof CategoryEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, CategoryEditorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editor);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        init();
        setListener(this);
    }

    private void init() {

    }

    @NonNull
    @Override
    public CategoryEditorPresenter createPresenter() {
        return new CategoryEditorPresenter();
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
