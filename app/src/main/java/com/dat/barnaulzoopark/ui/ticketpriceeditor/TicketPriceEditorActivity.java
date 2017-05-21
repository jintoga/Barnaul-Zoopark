package com.dat.barnaulzoopark.ui.ticketpriceeditor;

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
import com.dat.barnaulzoopark.model.TicketPrice;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 5/20/2017.
 */

public class TicketPriceEditorActivity extends
    BaseMvpPhotoEditActivity<TicketPriceEditorContract.View, TicketPriceEditorContract.UserActionListener>
    implements TicketPriceEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_TICKET_PRICE_UID = "EXTRA_SELECTED_TICKET_PRICE_UID";
    private static final int REQUEST_BROWSE_IMAGE = 111;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.name)
    protected EditText name;
    @Bind(R.id.price)
    protected EditText price;
    @Bind(R.id.icon)
    protected ImageView icon;
    @Bind(R.id.remove)
    protected ImageButton remove;
    @Bind(R.id.attach)
    protected ImageButton attach;

    private Uri iconUri;

    private TicketPrice selectedTicketPrice;

    private MaterialDialog progressDialog;

    public static void start(Context context, @Nullable String categoryUid) {
        if (context instanceof TicketPriceEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, TicketPriceEditorActivity.class);
        if (categoryUid != null) {
            intent.putExtra(EXTRA_SELECTED_TICKET_PRICE_UID, categoryUid);
        }
        context.startActivity(intent);
    }

    @Override
    public void highlightRequiredFields() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Input required");
        }
        if (price.getText().toString().isEmpty()) {
            price.setError("Input required");
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
    public void showEditingProgress() {
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, getString(R.string.updating));
        }
        progressDialog.setContent(getString(R.string.updating));
        progressDialog.show();
    }

    @Override
    public void onEditSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.edit_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onEditError(@NonNull String localizedMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, getString(R.string.loading));
        }
        progressDialog.setContent(getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    public void onLoadTicketPriceError(@NonNull String localizedMessage) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onLoadTicketPriceSuccess() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void bindSelectedTicketPrice(@NonNull TicketPrice ticketPrice) {
        this.selectedTicketPrice = ticketPrice;
        name.setText(ticketPrice.getName());
        price.setText(String.valueOf(ticketPrice.getPrice()));
        if (ticketPrice.getIcon() != null) {
            this.iconUri = Uri.parse(ticketPrice.getIcon());
            this.icon.setVisibility(View.VISIBLE);
            Glide.with(this).load(ticketPrice.getIcon()).into(icon);
            updateButtons(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_price_editor);
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
        String selectedTicketPriceUid = getIntent().getStringExtra(EXTRA_SELECTED_TICKET_PRICE_UID);
        if (selectedTicketPriceUid != null) {
            presenter.loadSelectedTicketPrice(selectedTicketPriceUid);
            updateTitle(getString(R.string.edit_ticket_price));
        } else {
            updateTitle(getString(R.string.create_ticket_price));
        }
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public TicketPriceEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new TicketPriceEditorPresenter(database, storage);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticket_price_editor, menu);
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
                if (selectedTicketPrice == null) {
                    createTicketPrice();
                } else {
                    editTicketPrice();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createTicketPrice() {
        presenter.createTicketPrice(name.getText().toString(), price.getText().toString(), iconUri);
    }

    private void editTicketPrice() {
        presenter.editTicketPrice(selectedTicketPrice, name.getText().toString(),
            price.getText().toString(), iconUri);
    }
}
