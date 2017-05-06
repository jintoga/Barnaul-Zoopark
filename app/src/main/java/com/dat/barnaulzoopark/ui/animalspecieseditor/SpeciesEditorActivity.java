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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
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

    private static final String EXTRA_SELECTED_SPECIES_UID = "EXTRA_SELECTED_SPECIES_UID";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.speciesEditorContent)
    protected RecyclerView speciesEditorContent;
    SpeciesEditorAdapter speciesEditorAdapter;

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

    }

    @Override
    public void onRemoveIconClicked() {

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

    }

    @Override
    public void onCropError(@NonNull String errorMsg) {

    }
}
