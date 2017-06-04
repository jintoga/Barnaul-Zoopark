package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Sponsor;
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

    @Override
    public void onRemovedPhotoClicked(int requestCode) {

    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {

    }

    @Override
    public void onCropError(@NonNull String errorMsg) {

    }
}
