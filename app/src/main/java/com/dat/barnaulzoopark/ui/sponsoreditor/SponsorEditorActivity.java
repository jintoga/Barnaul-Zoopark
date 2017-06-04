package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 6/4/2017.
 */

public class SponsorEditorActivity extends
    BaseMvpPhotoEditActivity<SponsorEditorContract.View, SponsorEditorContract.UserActionListener>
    implements SponsorEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_SPONSOR_UID = "EXTRA_SELECTED_SPONSOR_UID";
    private static final int REQUEST_BROWSE_IMAGE = 111;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.name)
    protected EditText name;
    @Bind(R.id.website)
    protected EditText website;
    @Bind(R.id.icon)
    protected ImageView icon;
    @Bind(R.id.remove)
    protected ImageButton remove;
    @Bind(R.id.attach)
    protected ImageButton attach;

    private Uri iconUri;

    private Sponsor selectedSponsor;

    private MaterialDialog progressDialog;

    public static void start(Context context, @Nullable String sponsorUid) {
        if (context instanceof SponsorEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, SponsorEditorActivity.class);
        if (sponsorUid != null) {
            intent.putExtra(EXTRA_SELECTED_SPONSOR_UID, sponsorUid);
        }
        context.startActivity(intent);
    }

    @Override
    public void highlightRequiredFields() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Input required");
        }
    }

    @Override
    public void onCreatingFailure(@NonNull String localizedMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onCreatingSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.created_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showCreatingProgress() {
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, getString(R.string.creating));
        }
        progressDialog.setContent(getString(R.string.creating));
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_editor);
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
        String selectedSponsorUid = getIntent().getStringExtra(EXTRA_SELECTED_SPONSOR_UID);
        if (selectedSponsorUid != null) {
            presenter.loadSelectedSponsor(selectedSponsorUid);
            updateTitle(getString(R.string.edit_sponsor));
        } else {
            updateTitle(getString(R.string.create_sponsor));
        }
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public SponsorEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new SponsorEditorPresenter(database, storage);
    }

    @OnClick(R.id.remove)
    protected void removeIconClicked() {
        this.iconUri = null;
        this.icon.setVisibility(View.GONE);
        this.icon.setImageDrawable(null);
        updateButtons(false);
    }

    @OnClick(R.id.attach)
    protected void attachIconClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE, iconUri != null);
    }

    private void updateButtons(boolean isFilled) {
        if (isFilled) {
            attach.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        } else {
            attach.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRemovedPhotoClicked(int requestCode) {

    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {
        if (originalRequestCode == REQUEST_BROWSE_IMAGE) {
            this.iconUri = uri;
            this.icon.setVisibility(View.VISIBLE);
            Glide.with(this).load(iconUri).into(icon);
            updateButtons(true);
        }
    }

    @Override
    public void onCropError(@NonNull String errorMsg) {
        showSnackBar(errorMsg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sponsor_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //ToDO: implement discard changes
                finish();
                break;
            case R.id.save:
                if (selectedSponsor == null) {
                    createSponsor();
                } else {
                    editSponsor();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editSponsor() {

    }

    private void createSponsor() {
        presenter.createSponsor(name.getText().toString(), website.getText().toString(), iconUri);
    }
}
