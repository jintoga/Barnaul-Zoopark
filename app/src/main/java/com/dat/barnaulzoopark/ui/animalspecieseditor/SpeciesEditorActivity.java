package com.dat.barnaulzoopark.ui.animalspecieseditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.animalspecieseditor.adapters.SpeciesEditorAdapter;
import com.dat.barnaulzoopark.ui.animalspecieseditor.adapters.SpeciesEditorHeaderAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.List;

/**
 * Created by DAT on 5/2/2017.
 */

public class SpeciesEditorActivity extends
    BaseMvpPhotoEditActivity<SpeciesEditorContract.View, SpeciesEditorContract.UserActionListener>
    implements SpeciesEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener,
    SpeciesEditorHeaderAdapter.IconClickListener,
    SpeciesEditorAdapter.RemoveAnimalFromSpeciesListener {

    private static final int REQUEST_BROWSE_IMAGE = 111;
    private static final String EXTRA_SELECTED_SPECIES_UID = "EXTRA_SELECTED_SPECIES_UID";
    private static final String TAG = SpeciesEditorActivity.class.getName();

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.speciesEditorContent)
    protected RecyclerView speciesEditorContent;

    private Species selectedSpecies;

    private MaterialDialog progressDialog;

    public static void start(Context context, @Nullable String speciesUid) {
        if (context instanceof SpeciesEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, SpeciesEditorActivity.class);
        if (speciesUid != null) {
            intent.putExtra(EXTRA_SELECTED_SPECIES_UID, speciesUid);
        }
        context.startActivity(intent);
    }

    @Override
    public void onAttachIconClicked() {
        RecyclerView.ViewHolder viewHolder =
            speciesEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {
            Uri iconUri = ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getIconUri();
            createChangePhotoDialog(REQUEST_BROWSE_IMAGE, iconUri != null);
        }
    }

    @Override
    public void onRemoveAnimalFromSpeciesClicked(@NonNull Animal animal) {

    }

    @Override
    public void onRemoveIconClicked() {
        RecyclerView.ViewHolder viewHolder =
            speciesEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {
            ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).clearIcon();
        }
    }

    @Override
    public void bindCategories(@NonNull List<Category> categories) {
        RecyclerView.ViewHolder viewHolder =
            speciesEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {
            ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).bindCategories(categories);
            if (selectedSpecies != null) {
                ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).bindSelectedSpecies(
                    selectedSpecies);
            }
        }
    }

    @Override
    public void bindSelectedSpecies(@NonNull Species selectedSpecies) {
        this.selectedSpecies = selectedSpecies;
    }

    @Override
    public void onCreatingSpeciesFailure(@NonNull String msg) {
        Log.d(TAG, "onCreatingCategoryFailure");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
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
    public void onEditError(@NonNull String localizedMessage) {
        Log.d(TAG, "onEditError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onEditSuccess() {
        Log.d(TAG, "onEditSuccess");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.edit_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showEditingProgress() {
        Log.d(TAG, "showEditingProgress");
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, "Updating animal species...");
        }
    }

    @Override
    public void onLoadCategoriesError(@NonNull String msg) {
        Log.d(TAG, "onLoadCategoriesError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    @Override
    public void onLoadSpeciesError(@NonNull String localizedMessage) {
        Log.d(TAG, "onLoadSpeciesError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onLoadSpeciesSuccess() {
        Log.d(TAG, "onLoadCategorySuccess");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showCreatingProgress() {
        Log.d(TAG, "showCreatingProgress");
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, "Creating animal species...");
        }
    }

    @Override
    public void highlightRequiredFields() {
        Log.d(TAG, "highlightRequiredFields");
        RecyclerView.ViewHolder viewHolder =
            speciesEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {
            ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).highlightRequiredFields();
        }
    }

    @Override
    public void showLoadingProgress() {
        Log.d(TAG, "showLoadingProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                "Loading selected animal species...");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_editor);
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
        String selectedSpeciesUid = getIntent().getStringExtra(EXTRA_SELECTED_SPECIES_UID);
        if (selectedSpeciesUid != null) {
            presenter.loadSelectedSpecies(selectedSpeciesUid);
            updateTitle(getString(R.string.edit_species));
        } else {
            updateTitle(getString(R.string.create_species));
        }
        initRecyclerView(selectedSpeciesUid);
        presenter.loadCategories();
    }

    private void initRecyclerView(@Nullable String selectedSpeciesUid) {
        speciesEditorContent.setLayoutManager(new LinearLayoutManager(this));
        SpeciesEditorAdapter speciesEditorAdapter =
            new SpeciesEditorAdapter(Animal.class, R.layout.item_species_editor,
                SpeciesEditorAdapter.ViewHolder.class,
                presenter.getChildAnimalsReference(selectedSpeciesUid), this);

        RecyclerView.Adapter wrappedAdapter =
            new SpeciesEditorHeaderAdapter(speciesEditorAdapter, this);
        wrappedAdapter.notifyDataSetChanged();
        speciesEditorContent.setAdapter(wrappedAdapter);
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public SpeciesEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new SpeciesEditorPresenter(database, storage);
    }

    @Override
    public void onRemovedPhotoClicked(int requestCode) {
    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {
        if (originalRequestCode == REQUEST_BROWSE_IMAGE) {
            RecyclerView.ViewHolder viewHolder =
                speciesEditorContent.findViewHolderForAdapterPosition(0);
            if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {
                ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).bindIcon(uri);
            }
        }
    }

    @Override
    public void onCropError(@NonNull String errorMsg) {
        showSnackBar(errorMsg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.species_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //ToDo: implement discard dialog
                finish();
                break;
            case R.id.save:
                if (selectedSpecies == null) {
                    createSpecies();
                } else {
                    editCategory();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editCategory() {
        RecyclerView.ViewHolder viewHolder =
            speciesEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {
            String categoryUid =
                ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getCategoryId();
            if (categoryUid != null) {
                String name = ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getName();
                String description =
                    ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getDescription();
                Uri iconUri =
                    ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getIconUri();
                presenter.editCategory(selectedSpecies, name, description, categoryUid, iconUri);
            } else {
                onCreatingSpeciesFailure(getString(R.string.categories_empty_error));
            }
        }
    }

    private void createSpecies() {
        RecyclerView.ViewHolder viewHolder =
            speciesEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof SpeciesEditorHeaderAdapter.HeaderViewHolder) {

            String categoryId =
                ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getCategoryId();
            if (categoryId != null) {
                String name = ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getName();
                String description =
                    ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getDescription();
                Uri iconUri =
                    ((SpeciesEditorHeaderAdapter.HeaderViewHolder) viewHolder).getIconUri();
                presenter.createSpecies(name, description, categoryId, iconUri);
            } else {
                onCreatingSpeciesFailure(getString(R.string.categories_empty_error));
            }
        }
    }
}
