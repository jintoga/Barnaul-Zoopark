package com.dat.barnaulzoopark.ui.ticketpriceeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
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

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

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

    }

    @Override
    public void onCropError(@NonNull String errorMsg) {

    }
}
