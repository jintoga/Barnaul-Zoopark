package com.dat.barnaulzoopark.ui.animaleditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.adapters.MultiFileAttachmentAdapter;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.animaleditor.adapters.AnimalEditorSpeciesSpinnerAdapter;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.dat.barnaulzoopark.widget.PrefixEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DAT on 3/11/2017.
 */

public class AnimalEditorActivity extends
    BaseMvpPhotoEditActivity<AnimalEditorContract.View, AnimalEditorContract.UserActionListener>
    implements AnimalEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_ANIMAL_UID = "EXTRA_SELECTED_ANIMAL_UID";
    private static final String TAG = AnimalEditorActivity.class.getName();
    private static final int REQUEST_BROWSE_IMAGE_BANNER = 1;
    private static final int REQUEST_BROWSE_IMAGE_ICON = 3;
    private static final int REQUEST_BROWSE_IMAGE_ATTACHMENT = 2;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.bannerImage)
    protected ImageView bannerImage;
    @Bind(R.id.icon)
    protected ImageView icon;
    @Bind(R.id.remove)
    protected ImageButton remove;
    @Bind(R.id.attach)
    protected ImageButton attach;
    @Bind(R.id.name)
    protected EditText name;
    @Bind(R.id.species)
    protected Spinner species;
    @Bind(R.id.isMale)
    protected RadioButton isMale;
    @Bind(R.id.dateOfBirth)
    protected EditText dateOfBirth;
    @Bind(R.id.aboutOurAnimal)
    protected EditText aboutOurAnimal;
    @Bind(R.id.album)
    protected RecyclerView album;
    private MultiFileAttachmentAdapter attachmentAdapter;
    @Bind(R.id.video)
    protected PrefixEditText video;

    private Uri bannerImageUri;
    private Uri iconUri;

    private int filledAttachmentCounter = 0;
    private int currentAttachmentPosition = 0;

    private Date selectedDateOfBirth;

    private MaterialDialog progressDialog;

    private Animal selectedAnimal;

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
    public void creatingProgress() {
        Log.d(TAG, "creatingProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this, "Creating animal...");
        }
    }

    @Override
    public void highlightRequiredFields() {
        Log.d(TAG, "highlightRequiredFields");
        if (name.getText().toString().isEmpty()) {
            name.setError("Input required");
        }
        if (aboutOurAnimal.getText().toString().isEmpty()) {
            aboutOurAnimal.setError("Input required");
        }
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

        initSpinners();
        initAttachmentsRecyclerView();
    }

    private void initAttachmentsRecyclerView() {
        album.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        album.addItemDecoration(new MultiAttachmentDecoration(
            (int) getResources().getDimension(R.dimen.item_file_attachment_decoration)));
        attachmentAdapter = new MultiFileAttachmentAdapter(this);
        album.setAdapter(attachmentAdapter);
        String selectedAnimalUid = getIntent().getStringExtra(EXTRA_SELECTED_ANIMAL_UID);
        if (selectedAnimalUid != null) {
            //ToDo: implement edit
            //loadSelectedAnimal(selectedAnimalUid);
            //attachmentAdapter.setEditingMode(true);
        } else {
            attachmentAdapter.addEmptySlot();
        }

        video.setPrefix(getString(R.string.youtube_prefix));
    }

    private void initSpinners() {
        AnimalEditorSpeciesSpinnerAdapter speciesSpinnerAdapter =
            new AnimalEditorSpeciesSpinnerAdapter(this, Species.class,
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item,
                presenter.getSpeciesReference());
        species.setAdapter(speciesSpinnerAdapter);
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
        album.requestFocus();
        if (filledAttachmentCounter == MultiFileAttachmentAdapter.MAX_NUMBER_ATTACHMENT) {
            attachmentAdapter.emptySlot(position);
            attachmentAdapter.addEmptySlot();
        } else {
            attachmentAdapter.emptySlot(position);
        }
        filledAttachmentCounter--;
    }

    @Override
    public void onSlotSelected(int position) {
        album.requestFocus();
        if (filledAttachmentCounter == MultiFileAttachmentAdapter.MAX_NUMBER_ATTACHMENT) {
            Toast.makeText(this, "Выбрано максимальное количество файлов", Toast.LENGTH_LONG)
                .show();
            return;
        }
        currentAttachmentPosition = position;
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_ATTACHMENT, false);
    }

    @Override
    public void onRemovedPhotoClicked() {
        Log.d(TAG, "onRemovedPhotoClicked");
        bannerImageUri = null;
        bannerImage.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.img_photo_gallery_placeholder));
    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {
        switch (originalRequestCode) {
            case REQUEST_BROWSE_IMAGE_BANNER:
                Log.d(TAG, "REQUEST_BROWSE_IMAGE_BANNER");
                bannerImageUri = uri;
                Glide.with(this).load(uri).into(bannerImage);
                break;
            case REQUEST_BROWSE_IMAGE_ICON:
                Log.d(TAG, "REQUEST_BROWSE_IMAGE_ICON");
                iconUri = uri;
                Glide.with(this).load(uri).into(icon);
                updateAttachIconButtons(true);
                break;
            case REQUEST_BROWSE_IMAGE_ATTACHMENT:
                Log.d(TAG, "REQUEST_BROWSE_IMAGE_ATTACHMENT");
                filledAttachmentCounter++;
                Attachment attachment = new Attachment(true, uri.toString());
                attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
                attachmentAdapter.addEmptySlot();
                album.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCropError(@NonNull String errorMsg) {
        Log.d(TAG, errorMsg);
        showSnackBar(errorMsg);
    }

    @OnClick(R.id.dateOfBirth)
    protected void dateOfBirthClicked() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog =
            DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    selectedDateOfBirth = calendar.getTime();
                    dateOfBirth.setText(ConverterUtils.DATE_FORMAT.format(selectedDateOfBirth));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    @OnClick(R.id.bannerImageContainer)
    protected void bannerImageContainerClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_BANNER, true);
    }

    @OnClick(R.id.attach)
    protected void attachIconClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_ICON, false);
    }

    @OnClick(R.id.remove)
    protected void removeIconClicked() {
        this.iconUri = null;
        this.icon.setImageDrawable(null);
        updateAttachIconButtons(false);
    }

    private void updateAttachIconButtons(boolean isFilled) {
        if (isFilled) {
            icon.setVisibility(View.VISIBLE);
            attach.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
            attach.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
    }

    private void showSnackBar(@NonNull String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.animal_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //ToDo: implement discard dialog
                break;
            case R.id.save:
                if (selectedAnimal == null) {
                    createAnimal();
                } else {
                    //ToDo: implement edit
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAnimal() {
        if (!species.getAdapter().isEmpty()) {
            Species selectedSpecies = (Species) species.getSelectedItem();
            presenter.createAnimal(name.getText().toString(), aboutOurAnimal.getText().toString(),
                selectedSpecies.getId(), isMale.isChecked(), selectedDateOfBirth, iconUri,
                bannerImageUri, attachmentAdapter.getData(), video.getText().toString());
        } else {

        }
    }
}
