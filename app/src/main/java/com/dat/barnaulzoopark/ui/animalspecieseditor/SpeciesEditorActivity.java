package com.dat.barnaulzoopark.ui.animalspecieseditor;

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
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.animalspecieseditor.adapters.SpeciesEditorAdapter;
import com.dat.barnaulzoopark.ui.animalspecieseditor.adapters.SpeciesEditorHeaderAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;

/**
 * Created by DAT on 5/2/2017.
 */

public class SpeciesEditorActivity extends
    BaseMvpPhotoEditActivity<SpeciesEditorContract.View, SpeciesEditorContract.UserActionListener>
    implements SpeciesEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener,
    SpeciesEditorHeaderAdapter.IconClickListener {

    private static final int REQUEST_BROWSE_IMAGE = 111;
    private static final String EXTRA_SELECTED_SPECIES_UID = "EXTRA_SELECTED_SPECIES_UID";
    private static final String TAG = SpeciesEditorActivity.class.getName();

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.speciesEditorContent)
    protected RecyclerView speciesEditorContent;
    SpeciesEditorAdapter speciesEditorAdapter;

    private Species selectedSpecies;

    private MaterialDialog progressDialog;

    //ToDO: implement universal editor and use
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
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE, false);
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
    public void onCreatingSpeciesFailure(@NonNull String msg) {
        Log.d(TAG, "onCreatingCategoryFailure");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    @Override
    public void onCreatingSpeciesSuccess() {
        Log.d(TAG, "onCreatingSpeciesSuccess");
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
    public void uploadingIconProgress() {
        Log.d(TAG, "uploadingIconProgress");
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
            updateTitle(getString(R.string.edit_species));
        } else {
            updateTitle(getString(R.string.create_species));
        }

        speciesEditorContent.setLayoutManager(new LinearLayoutManager(this));
        speciesEditorAdapter = new SpeciesEditorAdapter();
        speciesEditorAdapter.setData(new ArrayList<Animal>());
        RecyclerView.Adapter wrappedAdapter =
            new SpeciesEditorHeaderAdapter(this, speciesEditorAdapter, this,
                presenter.getCategoryReference());
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
    public void onRemovedPhotoClicked() {
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
                break;
            case R.id.save:
                if (selectedSpecies == null) {
                    createSpecies();
                } else {
                    //ToDo: implement edit
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(@NonNull String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
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
