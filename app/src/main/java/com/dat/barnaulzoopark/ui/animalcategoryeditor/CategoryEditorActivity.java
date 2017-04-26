package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters.CategoryEditorAdapter;
import com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters.CategoryEditorHeaderAdapter;
import java.util.ArrayList;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorActivity extends
    BaseMvpPhotoEditActivity<CategoryEditorContract.View, CategoryEditorContract.UserActionListener>
    implements CategoryEditorContract.View, BaseMvpPhotoEditActivity.Listener {

    private static final String EXTRA_SELECTED_CATEGORY_UID = "EXTRA_SELECTED_CATEGORY_UID";
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.species)
    protected RecyclerView species;
    CategoryEditorAdapter categoryEditorAdapter;

    //ToDO: implement universal editor and use
    public static void start(Context context, @Nullable String categoryUid) {
        if (context instanceof CategoryEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, CategoryEditorActivity.class);
        if (categoryUid != null) {
            intent.putExtra(EXTRA_SELECTED_CATEGORY_UID, categoryUid);
        }
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
        categoryEditorAdapter = new CategoryEditorAdapter();
        categoryEditorAdapter.setData(new ArrayList<Species>());
        RecyclerView.Adapter wrappedAdapter =
            new CategoryEditorHeaderAdapter(categoryEditorAdapter);
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
