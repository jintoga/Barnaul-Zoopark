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
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.TicketPrice;
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

    }

    private void editTicketPrice() {
    }
}
