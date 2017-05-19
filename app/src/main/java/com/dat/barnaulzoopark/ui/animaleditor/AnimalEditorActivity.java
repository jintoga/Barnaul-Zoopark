package com.dat.barnaulzoopark.ui.animaleditor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.adapters.BaseHintSpinnerAdapter;
import com.dat.barnaulzoopark.ui.adapters.MultiFileAttachmentAdapter;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.dat.barnaulzoopark.widget.PrefixEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DAT on 3/11/2017.
 */

public class AnimalEditorActivity extends
    BaseMvpPhotoEditActivity<AnimalEditorContract.View, AnimalEditorContract.UserActionListener>
    implements AnimalEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener, LocationDetectHelper.LocationDetectListener {

    private static final String EXTRA_SELECTED_ANIMAL_UID = "EXTRA_SELECTED_ANIMAL_UID";
    private static final String TAG = AnimalEditorActivity.class.getName();
    private static final int REQUEST_BROWSE_IMAGE_BANNER = 1;
    private static final int REQUEST_BROWSE_IMAGE_ICON = 3;
    private static final int REQUEST_BROWSE_IMAGE_ATTACHMENT = 2;
    private static final int REQUEST_BROWSE_IMAGE_HABITAT_MAP = 4;

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
    BaseHintSpinnerAdapter<Species> speciesSpinnerAdapter;
    @Bind(R.id.isMale)
    protected RadioButton isMale;
    @Bind(R.id.isFemale)
    protected RadioButton isFemale;
    @Bind(R.id.dateOfBirth)
    protected EditText dateOfBirth;
    @Bind(R.id.aboutOurAnimal)
    protected EditText aboutOurAnimal;
    @Bind(R.id.album)
    protected RecyclerView album;
    private MultiFileAttachmentAdapter attachmentAdapter;
    @Bind(R.id.video)
    protected PrefixEditText video;
    @Bind(R.id.habitatMapImage)
    protected ImageView habitatMapImage;
    @Bind(R.id.latLng)
    protected EditText latLng;

    private Uri bannerImageUri;
    private Uri iconUri;
    private Uri habitatMapImageUri;

    private int filledAttachmentCounter = 0;
    private int currentAttachmentPosition = 0;
    private Double lat, lng;

    private Date selectedDateOfBirth;

    private MaterialDialog progressDialog;

    private Animal selectedAnimal;

    private LocationDetectHelper locationDetectHelper;

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
    public void bindSpecies(@NonNull List<Species> speciesList) {
        initSpinner(speciesList);
    }

    @Override
    public void onLoadSpeciesError(@NonNull String message) {
        Log.d(TAG, "onLoadSpeciesError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(message);
    }

    @Override
    public void creatingProgress() {
        Log.d(TAG, "creatingProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.creating_animal));
        }
        progressDialog.setContent(getString(R.string.creating_animal));
        progressDialog.show();
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
    public void onCreatingFailure(@NonNull String msg) {
        Log.d(TAG, "onCreatingFailure " + msg);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    @Override
    public void onCreatingSuccess() {
        Log.d(TAG, "onCreatingSuccess");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showToast("Animal created successful");
        finish();
    }

    @Override
    public void onLoadAnimalError(@NonNull String localizedMessage) {
        Log.d(TAG, "onLoadAnimalError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onLoadAnimalSuccess() {
        Log.d(TAG, "onLoadAnimalSuccess");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showEditingProgress() {
        Log.d(TAG, "showEditingProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.updating_animal));
        }
        progressDialog.setContent(getString(R.string.updating_animal));
        progressDialog.show();
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
    public void showLoadingProgress() {
        Log.d(TAG, "showLoadingProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.loading_animal));
        }
        progressDialog.setContent(getString(R.string.loading_animal));
        progressDialog.show();
    }

    @Override
    public void bindSelectedAnimal(@NonNull Animal selectedAnimal) {
        this.selectedAnimal = selectedAnimal;
        for (int i = 1; i < speciesSpinnerAdapter.getData().size();
            i++) { //position 0 is hint so start from 1
            if (speciesSpinnerAdapter.getData()
                .get(i)
                .getUid()
                .equals(selectedAnimal.getSpeciesUid())) {
                species.setSelection(i);
                break;
            }
        }
        name.setText(selectedAnimal.getName());
        aboutOurAnimal.setText(selectedAnimal.getAboutOurAnimal());
        isMale.setChecked(selectedAnimal.isGender());
        isFemale.setChecked(!selectedAnimal.isGender());
        if (selectedAnimal.getDateOfBirth() != null) {
            selectedDateOfBirth = new Date(selectedAnimal.getDateOfBirth());
            dateOfBirth.setText(ConverterUtils.DATE_FORMAT.format(selectedDateOfBirth));
        }
        if (selectedAnimal.getLat() != null && selectedAnimal.getLng() != null) {
            bindLatLng(selectedAnimal.getLat(), selectedAnimal.getLng());
        }
        if (selectedAnimal.getVideo() != null) {
            video.setText(selectedAnimal.getVideo());
        }
        if (selectedAnimal.getPhotoSmall() != null) {
            iconUri = Uri.parse(selectedAnimal.getPhotoSmall());
            Glide.with(this).load(iconUri).into(icon);
            updateAttachIconButtons(true);
        }
        if (selectedAnimal.getPhotoBig() != null) {
            bannerImageUri = Uri.parse(selectedAnimal.getPhotoBig());
            Glide.with(this).load(bannerImageUri).into(bannerImage);
        }
        if (selectedAnimal.getImageHabitatMap() != null) {
            habitatMapImageUri = Uri.parse(selectedAnimal.getImageHabitatMap());
            Glide.with(this).load(habitatMapImageUri).into(habitatMapImage);
        }
        for (Map.Entry<String, String> entry : selectedAnimal.getPhotos().entrySet()) {
            filledAttachmentCounter++;
            Attachment attachment = new Attachment(true, entry.getValue());
            attachment.setAttachmentUid(entry.getKey());
            attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
            attachmentAdapter.addEmptySlot();
            album.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
            currentAttachmentPosition++;
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

    @Override
    protected void onStop() {
        super.onStop();
        if (locationDetectHelper != null) {
            locationDetectHelper.disconnectGoogleApiClient();
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        switch (permsRequestCode) {
            case LocationDetectHelper.REQUEST_LOCATION_PERMISSIONS:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (locationDetectHelper != null) {
                        locationDetectHelper.startDetecting();
                    }
                } else {
                    if (locationDetectHelper != null) {
                        locationDetectHelper.disconnectGoogleApiClient();
                    }
                    showSnackBar("Permission was not granted");
                }
                break;
        }
    }

    @OnClick(R.id.latLng)
    protected void latLngClicked() {
        if (locationDetectHelper == null) {
            locationDetectHelper = new LocationDetectHelper(this, this);
            locationDetectHelper.connectGoogleApiClient();
            return;
        }
        locationDetectHelper.reconnectGoogleApiClient();
    }

    @Override
    public void onLocationChanged(double currentLatitude, double currentLongitude) {
        bindLatLng(currentLatitude, currentLongitude);
        showSnackBar(getString(R.string.location_updated));
    }

    private void bindLatLng(double currentLatitude, double currentLongitude) {
        this.lat = currentLatitude;
        this.lng = currentLongitude;
        String currentLocation = String.format("%s — %s", currentLatitude, currentLongitude);
        latLng.setText(currentLocation);
    }

    @Override
    public void onNoResolutionForConnectionFailed(@NonNull String msg) {
        showSnackBar(msg);
    }

    private void init() {
        initAttachmentsRecyclerView();
        presenter.loadSpecies();
        String selectedAnimalUid = getIntent().getStringExtra(EXTRA_SELECTED_ANIMAL_UID);
        if (selectedAnimalUid != null) {
            presenter.loadSelectedAnimal(selectedAnimalUid);
            attachmentAdapter.setEditingMode(true);
            updateTitle(getString(R.string.edit_animal));
        } else {
            updateTitle(getString(R.string.create_animal));
        }
    }

    private void initAttachmentsRecyclerView() {
        album.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        album.addItemDecoration(new MultiAttachmentDecoration(
            (int) getResources().getDimension(R.dimen.item_file_attachment_decoration)));
        attachmentAdapter = new MultiFileAttachmentAdapter(this);
        album.setAdapter(attachmentAdapter);
        attachmentAdapter.addEmptySlot();

        video.setPrefix(getString(R.string.youtube_prefix));
    }

    private void initSpinner(@NonNull List<Species> speciesList) {
        speciesSpinnerAdapter = new BaseHintSpinnerAdapter<Species>(this, speciesList) {
            @Override
            protected String getItemStringValue(@NonNull Species species) {
                return species.getName();
            }
        };
        speciesSpinnerAdapter.setHint(getString(R.string.select_species_hint));
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
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_ATTACHMENT);
    }

    @Override
    public void onRemovedPhotoClicked(int requestCode) {
        Log.d(TAG, "onRemovedPhotoClicked");
        if (requestCode == REQUEST_BROWSE_IMAGE_BANNER) {
            bannerImageUri = null;
            bannerImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.img_photo_gallery_placeholder));
        } else if (requestCode == REQUEST_BROWSE_IMAGE_HABITAT_MAP) {
            habitatMapImageUri = null;
            habitatMapImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.img_photo_gallery_placeholder));
        }
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
            case REQUEST_BROWSE_IMAGE_HABITAT_MAP:
                Log.d(TAG, "REQUEST_BROWSE_IMAGE_BANNER");
                habitatMapImageUri = uri;
                Glide.with(this).load(uri).into(habitatMapImage);
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
        if (selectedDateOfBirth != null) {
            calendar.setTime(selectedDateOfBirth);
        }
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
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_BANNER, bannerImageUri != null);
    }

    @OnClick(R.id.habitatMapImageContainer)
    protected void habitatMapImageContainerClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_HABITAT_MAP, habitatMapImageUri != null);
    }

    @OnClick(R.id.attach)
    protected void attachIconClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_ICON, iconUri != null);
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
                finish();
                break;
            case R.id.save:
                if (selectedAnimal == null) {
                    createAnimal();
                } else {
                    editAnimal();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editAnimal() {
        if (getSpeciesUid() != null) {
            presenter.editAnimal(selectedAnimal, name.getText().toString(),
                aboutOurAnimal.getText().toString(), getSpeciesUid(), isMale.isChecked(),
                selectedDateOfBirth, iconUri, bannerImageUri, habitatMapImageUri,
                attachmentAdapter.getItemsToAdd(), attachmentAdapter.getItemsToDelete(),
                video.getText().toString(), lat, lng);
        } else {
            onEditError(getString(R.string.species_invalid_error));
        }
    }

    private void createAnimal() {
        if (getSpeciesUid() != null) {
            presenter.createAnimal(name.getText().toString(), aboutOurAnimal.getText().toString(),
                getSpeciesUid(), isMale.isChecked(), selectedDateOfBirth, iconUri, bannerImageUri,
                habitatMapImageUri, attachmentAdapter.getData(), video.getText().toString(), lat,
                lng);
        } else {
            onCreatingFailure(getString(R.string.species_invalid_error));
        }
    }

    @Nullable
    public String getSpeciesUid() {
        if (species.getSelectedItemPosition() > 0) {
            Species selectedSpecies = (Species) species.getSelectedItem();
            return selectedSpecies.getUid();
        }
        return null;
    }
}
