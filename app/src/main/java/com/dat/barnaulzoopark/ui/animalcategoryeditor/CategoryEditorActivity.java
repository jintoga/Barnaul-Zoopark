package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters.CategoryEditorAdapter;
import com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters.CategoryEditorHeaderAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorActivity extends
    BaseMvpPhotoEditActivity<CategoryEditorContract.View, CategoryEditorContract.UserActionListener>
    implements CategoryEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener,
    CategoryEditorHeaderAdapter.IconClickListener {
    private static final int REQUEST_BROWSE_IMAGE = 111;
    private static final String TAG = CategoryEditorActivity.class.getName();

    private static final String EXTRA_SELECTED_CATEGORY_UID = "EXTRA_SELECTED_CATEGORY_UID";
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.categoryEditorContent)
    protected RecyclerView categoryEditorContent;
    CategoryEditorAdapter categoryEditorAdapter;

    private Category selectedCategory;

    private MaterialDialog progressDialog;

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
    public void highlightRequiredFields() {
        Log.d(TAG, "highlightRequiredFields");
        RecyclerView.ViewHolder viewHolder =
            categoryEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof CategoryEditorHeaderAdapter.HeaderViewHolder) {
            ((CategoryEditorHeaderAdapter.HeaderViewHolder) viewHolder).highlightRequiredFields();
        }
    }

    @Override
    public void onCreatingCategoryFailure(@NonNull String msg) {
        Log.d(TAG, "onCreatingCategoryFailure");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    @Override
    public void onCreatingCategorySuccess() {
        Log.d(TAG, "onCreatingCategorySuccess");
    }

    @Override
    public void onCreatingComplete() {
        Log.d(TAG, "onCreatingComplete");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.created_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUploadFailure(@NonNull String msg) {
        Log.d(TAG, "onUploadFailure" + msg);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    private void showSnackBar(@NonNull String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onAttachIconClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE, false);
    }

    @Override
    public void onRemoveIconClicked() {
        RecyclerView.ViewHolder viewHolder =
            categoryEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof CategoryEditorHeaderAdapter.HeaderViewHolder) {
            ((CategoryEditorHeaderAdapter.HeaderViewHolder) viewHolder).clearIcon();
        }
    }

    @Override
    public void showCreatingProgress() {
        Log.d(TAG, "showCreatingProgress");
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, "Creating animal category...");
        }
    }

    @Override
    public void uploadingIconProgress() {

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
        setPhotoEditListener(this);
    }

    private void init() {
        String selectedNewsUid = getIntent().getStringExtra(EXTRA_SELECTED_CATEGORY_UID);
        if (selectedNewsUid != null) {
            updateTitle(getString(R.string.edit_category));
        } else {
            updateTitle(getString(R.string.create_category));
        }

        categoryEditorContent.setLayoutManager(new LinearLayoutManager(this));
        categoryEditorAdapter = new CategoryEditorAdapter();
        categoryEditorAdapter.setData(new ArrayList<Species>());
        RecyclerView.Adapter wrappedAdapter =
            new CategoryEditorHeaderAdapter(categoryEditorAdapter, this);
        wrappedAdapter.notifyDataSetChanged();
        categoryEditorContent.setAdapter(wrappedAdapter);
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public CategoryEditorPresenter createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new CategoryEditorPresenter(database, storage);
    }

    @Override
    public void onRemovedPhotoClicked() {
    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {
        if (originalRequestCode == REQUEST_BROWSE_IMAGE) {
            RecyclerView.ViewHolder viewHolder =
                categoryEditorContent.findViewHolderForAdapterPosition(0);
            if (viewHolder instanceof CategoryEditorHeaderAdapter.HeaderViewHolder) {
                ((CategoryEditorHeaderAdapter.HeaderViewHolder) viewHolder).bindIcon(uri);
            }
        }
    }

    @Override
    public void onCropError(@NonNull String errorMsg) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.save:
                if (selectedCategory == null) {
                    createCategory();
                } else {
                    //ToDo: implement edit
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCategory() {
        RecyclerView.ViewHolder viewHolder =
            categoryEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof CategoryEditorHeaderAdapter.HeaderViewHolder) {
            String name = ((CategoryEditorHeaderAdapter.HeaderViewHolder) viewHolder).getName();
            String description =
                ((CategoryEditorHeaderAdapter.HeaderViewHolder) viewHolder).getDescription();
            Uri iconUri = ((CategoryEditorHeaderAdapter.HeaderViewHolder) viewHolder).getIconUri();
            presenter.createCategory(name, description, iconUri);
        }
    }
}
