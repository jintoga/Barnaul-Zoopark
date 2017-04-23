package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.DummyGenerator;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.animalsdetail.AnimalsImagesHorizontalAdapter;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorActivity extends
    BaseMvpPhotoEditActivity<CategoryEditorContract.View, CategoryEditorContract.UserActionListener>
    implements CategoryEditorContract.View, BaseMvpPhotoEditActivity.Listener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.species)
    protected RecyclerView species;
    AnimalsImagesHorizontalAdapter animalsImagesAdapter;

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
        species.setLayoutManager(new LinearLayoutManager(this));
        animalsImagesAdapter = new AnimalsImagesHorizontalAdapter();
        animalsImagesAdapter.setData(DummyGenerator.getAnimalsPhotos());
        RecyclerView.Adapter wrappedAdapter =
            new CategoryEditorContentHeaderAdapter(animalsImagesAdapter);
        wrappedAdapter.notifyDataSetChanged();
        species.setAdapter(wrappedAdapter);
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
