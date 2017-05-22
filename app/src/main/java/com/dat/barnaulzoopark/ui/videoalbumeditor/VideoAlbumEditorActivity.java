package com.dat.barnaulzoopark.ui.videoalbumeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.dat.barnaulzoopark.ui.videoalbumeditor.adapters.VideoAlbumEditorAdapter;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;
import java.util.Map;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbumEditorActivity extends
    BaseMvpActivity<VideoAlbumEditorContract.View, VideoAlbumEditorContract.UserActionListener>
    implements VideoAlbumEditorContract.View, VideoAlbumEditorAdapter.AttachmentListener {

    private static final String EXTRA_SELECTED_VIDEO_ALBUM_UID = "EXTRA_SELECTED_PHOTO_ALBUM_UID";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.videoAlbumEditorContent)
    protected RecyclerView videoAlbumEditorContent;
    private VideoAlbumEditorAdapter attachmentAdapter;

    private VideoAlbum selectedVideoAlbum;

    private MaterialDialog progressDialog;

    private int filledAttachmentCounter = 0;
    private int currentAttachmentPosition = 0;

    public static void start(Context context, @Nullable String videoAlbumUid) {
        if (context instanceof VideoAlbumEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, VideoAlbumEditorActivity.class);
        if (videoAlbumUid != null) {
            intent.putExtra(EXTRA_SELECTED_VIDEO_ALBUM_UID, videoAlbumUid);
        }
        context.startActivity(intent);
    }

    @Override
    public void onRemoved(int position) {
        attachmentAdapter.emptySlot(position);
        filledAttachmentCounter--;
    }

    @Override
    public void onSlotSelected(String videoId, int position) {
        currentAttachmentPosition = position;
        filledAttachmentCounter++;
        Attachment attachment = new Attachment(true, videoId);
        attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
        attachmentAdapter.addEmptySlot();
        videoAlbumEditorContent.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_album_editor);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        init();
    }

    private void init() {
        String selectedVideoAlbumUid = getIntent().getStringExtra(EXTRA_SELECTED_VIDEO_ALBUM_UID);
        if (selectedVideoAlbumUid != null) {
            presenter.loadSelectedVideoAlbum(selectedVideoAlbumUid);
            updateTitle(getString(R.string.edit_video_album));
        } else {
            updateTitle(getString(R.string.create_video_album));
        }
        initRecyclerView(selectedVideoAlbumUid);
    }

    private void initRecyclerView(@Nullable String selectedPhotoAlbumUid) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        videoAlbumEditorContent.setLayoutManager(layoutManager);
        attachmentAdapter = new VideoAlbumEditorAdapter(this, this);
        videoAlbumEditorContent.setAdapter(attachmentAdapter);
        attachmentAdapter.addEmptySlot();
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public VideoAlbumEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        return new VideoAlbumEditorPresenter(database);
    }

    @Override
    public void highlightRequiredFields() {
        RecyclerView.ViewHolder viewHolder =
            videoAlbumEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof VideoAlbumEditorAdapter.HeaderViewHolder) {
            ((VideoAlbumEditorAdapter.HeaderViewHolder) viewHolder).highlightRequiredFields();
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
    public void onEditError(@NonNull String localizedMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
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
    public void showLoadingProgress() {
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, getString(R.string.loading));
        }
        progressDialog.setContent(getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    public void bindSelectedPhotoAlbum(@NonNull VideoAlbum videoAlbum) {
        this.selectedVideoAlbum = videoAlbum;
        RecyclerView.ViewHolder viewHolder =
            videoAlbumEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof VideoAlbumEditorAdapter.HeaderViewHolder) {
            ((VideoAlbumEditorAdapter.HeaderViewHolder) viewHolder).bindSelectedPhotoAlbum(
                videoAlbum);
        }
        attachmentAdapter.setEditingMode(true);
        for (Map.Entry<String, String> entry : videoAlbum.getVideos().entrySet()) {
            filledAttachmentCounter++;
            Attachment attachment = new Attachment(true, entry.getValue());
            attachment.setAttachmentUid(entry.getKey());
            attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
            attachmentAdapter.addEmptySlot();
            videoAlbumEditorContent.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
            currentAttachmentPosition++;
        }
    }

    @Override
    public void onLoadError(@NonNull String localizedMessage) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onLoadSuccess() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_album_editor, menu);
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
                if (selectedVideoAlbum == null) {
                    createVideoAlbum();
                } else {
                    editVideoAlbum();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editVideoAlbum() {

    }

    private void createVideoAlbum() {
        if (attachmentAdapter.getFilledData().isEmpty()) {
            showSnackBar("No video attached!");
            return;
        }
        RecyclerView.ViewHolder viewHolder =
            videoAlbumEditorContent.findViewHolderForAdapterPosition(0);
        if (viewHolder instanceof VideoAlbumEditorAdapter.HeaderViewHolder) {
            String name = ((VideoAlbumEditorAdapter.HeaderViewHolder) viewHolder).getName();
            Date date = ((VideoAlbumEditorAdapter.HeaderViewHolder) viewHolder).getDate();
            presenter.createVideoAlbum(name, date, attachmentAdapter.getFilledData());
        }
    }
}
