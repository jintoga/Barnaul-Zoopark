package com.dat.barnaulzoopark.ui.photoalbumeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.photoalbumeditor.adapters.PhotoAlbumEditorAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.Date;

/**
 * Created by DAT on 5/21/2017.
 */

public class PhotoAlbumEditorActivity extends
    BaseMvpPhotoEditActivity<PhotoAlbumEditorContract.View, PhotoAlbumEditorContract.UserActionListener>
    implements PhotoAlbumEditorContract.View, BaseMvpPhotoEditActivity.PhotoEditListener,
    PhotoAlbumEditorAdapter.AttachmentListener {

    private static final String EXTRA_SELECTED_PHOTO_ALBUM_UID = "EXTRA_SELECTED_PHOTO_ALBUM_UID";

    private static final int REQUEST_BROWSE_IMAGE_ATTACHMENT = 111;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.photoAlbumEditorContent)
    protected RecyclerView photoAlbumEditorContent;
    private PhotoAlbumEditorAdapter attachmentAdapter;

    private PhotoAlbum selectedPhotoAlbum;

    private MaterialDialog progressDialog;

    private int filledAttachmentCounter = 0;
    private int currentAttachmentPosition = 0;

    public static void start(Context context, @Nullable String photoAlbumUid) {
        if (context instanceof PhotoAlbumEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, PhotoAlbumEditorActivity.class);
        if (photoAlbumUid != null) {
            intent.putExtra(EXTRA_SELECTED_PHOTO_ALBUM_UID, photoAlbumUid);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album_editor);
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
        String selectedPhotoAlbumUid = getIntent().getStringExtra(EXTRA_SELECTED_PHOTO_ALBUM_UID);
        if (selectedPhotoAlbumUid != null) {
            presenter.loadSelectedPhotoAlbum(selectedPhotoAlbumUid);
            updateTitle(getString(R.string.edit_photo_album));
        } else {
            updateTitle(getString(R.string.create_photo_album));
        }
        initRecyclerView(selectedPhotoAlbumUid);
    }

    private void initRecyclerView(@Nullable String selectedPhotoAlbumUid) {
        StaggeredGridLayoutManager layoutManager =
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        photoAlbumEditorContent.setLayoutManager(layoutManager);
        attachmentAdapter = new PhotoAlbumEditorAdapter(this, this);
        photoAlbumEditorContent.setAdapter(attachmentAdapter);
        attachmentAdapter.addEmptySlot();
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public PhotoAlbumEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new PhotoAlbumEditorPresenter(database, storage);
    }

    @Override
    public void onRemoved(int position) {
        attachmentAdapter.emptySlot(position);
        filledAttachmentCounter--;
    }

    @Override
    public void onSlotSelected(int position) {
        if (filledAttachmentCounter == PhotoAlbumEditorAdapter.MAX_NUMBER_ATTACHMENT) {
            Toast.makeText(this, "Выбрано максимальное количество файлов", Toast.LENGTH_LONG)
                .show();
            return;
        }
        currentAttachmentPosition = position;
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_ATTACHMENT);
    }

    @Override
    public void onRemovedPhotoClicked(int requestCode) {
    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {
        if (originalRequestCode == REQUEST_BROWSE_IMAGE_ATTACHMENT) {
            filledAttachmentCounter++;
            Attachment attachment = new Attachment(true, uri.toString());
            attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
            attachmentAdapter.addEmptySlot();
            photoAlbumEditorContent.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onCropError(@NonNull String errorMsg) {
        showSnackBar(errorMsg);
    }

    @Override
    public void highlightRequiredFields() {
        RecyclerView.ViewHolder viewHolder =
            photoAlbumEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof PhotoAlbumEditorAdapter.HeaderViewHolder) {
            ((PhotoAlbumEditorAdapter.HeaderViewHolder) viewHolder).highlightRequiredFields();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_album_editor, menu);
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
                if (selectedPhotoAlbum == null) {
                    createCategory();
                } else {
                    editCategory();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCategory() {
        RecyclerView.ViewHolder viewHolder =
            photoAlbumEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof PhotoAlbumEditorAdapter.HeaderViewHolder) {
            String name = ((PhotoAlbumEditorAdapter.HeaderViewHolder) viewHolder).getName();
            Date date = ((PhotoAlbumEditorAdapter.HeaderViewHolder) viewHolder).getDate();
            presenter.createPhotoAlbum(name, date, attachmentAdapter.getData());
        }
    }

    private void editCategory() {

    }
}
