package com.dat.barnaulzoopark.ui.animaleditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.adapters.MultiFileAttachmentAdapter;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.widget.PrefixEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 3/11/2017.
 */

public class AnimalEditorActivity extends
    BaseMvpPhotoEditActivity<AnimalEditorContract.View, AnimalEditorContract.UserActionListener>
    implements AnimalEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_ANIMAL_UID = "EXTRA_SELECTED_ANIMAL_UID";
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;
    @Bind(R.id.name)
    protected EditText name;
    @Bind(R.id.species)
    protected Spinner species;
    @Bind(R.id.gender)
    protected Spinner gender;
    @Bind(R.id.aboutOurAnimal)
    protected EditText aboutOurAnimal;
    @Bind(R.id.album)
    protected RecyclerView album;
    private MultiFileAttachmentAdapter attachmentAdapter;
    @Bind(R.id.video)
    protected PrefixEditText video;

    public static void start(@NonNull Context context, @Nullable String animalUid) {
        if (context instanceof AnimalEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, AnimalEditorActivity.class);
        if (animalUid != null) {
            intent.putExtra(EXTRA_SELECTED_ANIMAL_UID, animalUid);
        }
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
        String selectedAnimalUid = getIntent().getStringExtra(EXTRA_SELECTED_ANIMAL_UID);
        if (selectedAnimalUid != null) {
            updateTitle(getString(R.string.edit_animal));
        } else {
            updateTitle(getString(R.string.create_animal));
        }
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public AnimalEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new AnimalEditorPresenter(database, storage);
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
