package com.dat.barnaulzoopark.ui.newseditor;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.adapters.MultiFileAttachmentAdapter;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.dat.barnaulzoopark.widget.PrefixEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorActivity extends
    BaseMvpPhotoEditActivity<NewsItemEditorContract.View, NewsItemEditorContract.UserActionListener>
    implements NewsItemEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String EXTRA_SELECTED_NEWS_UID = "SELECTED_NEWS_UID";
    private static final String KEY_SAVED_ATTACHMENTS = "SAVED_ATTACHMENTS";
    private static final String KEY_SAVED_THUMBNAIL_URI = "SAVED_THUMBNAIL_URI";
    private static final int REQUEST_BROWSE_IMAGE_THUMBNAIL = 1;
    private static final int REQUEST_BROWSE_IMAGE_ATTACHMENT = 2;

    private static final String TAG = NewsItemEditorActivity.class.getName();
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;
    @Bind(R.id.title)
    protected EditText title;
    @Bind(R.id.description)
    protected EditText description;
    @Bind(R.id.album)
    protected RecyclerView album;
    private MultiFileAttachmentAdapter attachmentAdapter;
    @Bind(R.id.video)
    protected PrefixEditText video;

    private Uri thumbnailUri;

    private int filledAttachmentCounter = 0;
    private int currentAttachmentPosition = 0;

    private MaterialDialog progressDialog;

    private News selectedNews;

    public static void start(Context context, @Nullable String uid) {
        if (context instanceof NewsItemEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, NewsItemEditorActivity.class);
        if (uid != null) {
            intent.putExtra(EXTRA_SELECTED_NEWS_UID, uid);
        }
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public NewsItemEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new NewsItemEditorPresenter(database, storage);
    }

    @Override
    public void onRemovedPhotoClicked(int requestCode) {
        Log.d(TAG, "onRemovedPhotoClicked");
        thumbnailUri = null;
        thumbnail.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.img_photo_gallery_placeholder));
    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode) {
        switch (originalRequestCode) {
            case REQUEST_BROWSE_IMAGE_THUMBNAIL:
                Log.d(TAG, "REQUEST_BROWSE_IMAGE_THUMBNAIL");
                thumbnailUri = uri;
                Glide.with(this).load(uri).into(thumbnail);
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
        showSnackBar(errorMsg);
    }

    @Override
    public void bindSelectedNews(@NonNull News selectedNews) {
        this.selectedNews = selectedNews;
        if (selectedNews.getThumbnail() != null) {
            thumbnailUri = Uri.parse(selectedNews.getThumbnail());
        }
        title.setText(selectedNews.getTitle());
        description.setText(selectedNews.getDescription());
        video.setText(selectedNews.getVideo());
        if (selectedNews.getThumbnail() != null && !"".equals(selectedNews.getThumbnail())) {
            Glide.with(this).load(Uri.parse(selectedNews.getThumbnail())).into(thumbnail);
        }
        if (selectedNews.getPhotos() != null && !selectedNews.getPhotos().isEmpty()) {
            List<Attachment> attachments = new ArrayList<>();
            for (Map.Entry<String, String> entry : selectedNews.getPhotos().entrySet()) {
                Attachment attachment = new Attachment(true, entry.getValue());
                attachment.setAttachmentUid(entry.getKey());
                attachments.add(attachment);
            }
            attachmentAdapter.setData(attachments);
            filledAttachmentCounter = attachments.size();
        }
        attachmentAdapter.addEmptySlot();
    }

    @Override
    public void highlightRequiredFields() {
        Log.d(TAG, "highlightRequiredFields");
        if (title.getText().toString().isEmpty()) {
            title.setError("Input required");
        }
        if (description.getText().toString().isEmpty()) {
            description.setError("Input required");
        }
    }

    @Override
    public void onUpdatingComplete() {
        Log.d(TAG, "onUpdatingComplete");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();
    }

    @Override
    public void onUpdatingNewsFailure(@NonNull String errorMsg) {
        Log.d(TAG, "onCreatingNewsItemFailure " + errorMsg);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(errorMsg);
    }

    @Override
    public void onUpdatingNewsSuccess() {
        Log.d(TAG, "onUpdatingNewsSuccess");
    }

    @Override
    public void onUploadFailure(@NonNull String errorMsg) {
        Log.d(TAG, "onUploadFailure" + errorMsg);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        showSnackBar(errorMsg);
    }

    @Override
    public void onCreatingComplete() {
        Log.d(TAG, "onCreatingComplete");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();
    }

    @Override
    public void creatingNewsItemProgress() {
        Log.d(TAG, "creatingNewsItemProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.creating_news_item));
        }
        progressDialog.setContent(getString(R.string.creating_news_item));
        progressDialog.show();
    }

    @Override
    public void onCreatingNewsItemSuccess() {
        Log.d(TAG, "onCreatingNewsItemSuccess");
    }

    @Override
    public void onCreatingNewsItemFailure(@NonNull String errorMsg) {
        Log.d(TAG, "onCreatingNewsItemFailure " + errorMsg);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(errorMsg);
    }

    @Override
    public void updatingNewsItemProgress() {
        Log.d(TAG, "updatingNewsItemProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.updating_news_item));
        }
        progressDialog.setContent(getString(R.string.updating_news_item));
        progressDialog.show();
    }

    @Override
    public void uploadingAttachments() {
        Log.d(TAG, "uploadingAttachments");
    }

    @Override
    public void uploadingThumbnailProgress() {
        Log.d(TAG, "uploadingThumbnailProgress");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item_editor);
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
        album.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        album.addItemDecoration(new MultiAttachmentDecoration(
            (int) getResources().getDimension(R.dimen.item_file_attachment_decoration)));
        attachmentAdapter = new MultiFileAttachmentAdapter(this);
        album.setAdapter(attachmentAdapter);
        String selectedNewsUid = getIntent().getStringExtra(EXTRA_SELECTED_NEWS_UID);
        if (selectedNewsUid != null) {
            loadSelectedNews(selectedNewsUid);
            updateTitle(getString(R.string.edit_news_item));
            attachmentAdapter.setEditingMode(true);
        } else {
            updateTitle(getString(R.string.create_news_item));
            attachmentAdapter.addEmptySlot();
        }

        video.setPrefix(getString(R.string.youtube_prefix));
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void loadSelectedNews(@NonNull String selectedNewsUid) {
        presenter.loadSelectedNews(selectedNewsUid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_item_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                checkDiscardConfirmRequirement();
                break;
            case R.id.save:
                if (selectedNews == null) {
                    presenter.createNewsItem(title.getText().toString(),
                        description.getText().toString(), thumbnailUri, attachmentAdapter.getData(),
                        video.getText().toString());
                } else {
                    if (isModified()) {
                        Log.d(TAG, "UPDATING");
                        presenter.updateSelectedNewsItem(selectedNews, title.getText().toString(),
                            description.getText().toString(), thumbnailUri,
                            attachmentAdapter.getItemsToAdd(), attachmentAdapter.getItemsToDelete(),
                            video.getText().toString());
                    } else {
                        finish();
                    }
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isModified() {
        if (!title.getText().toString().equals(selectedNews.getTitle())) {
            return true;
        }
        if (!description.getText().toString().equals(selectedNews.getDescription())) {
            return true;
        }
        if (selectedNews.getThumbnail() != null) {
            if (thumbnailUri == null || !thumbnailUri.toString()
                .equals(selectedNews.getThumbnail())) {
                return true;
            }
        } else if (thumbnailUri != null) {
            return true;
        }
        if (attachmentAdapter.isModified()) {
            return true;
        }
        if (!video.getText().toString().equals(selectedNews.getVideo())) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        checkDiscardConfirmRequirement();
    }

    private void showDiscardConfirm() {
        BZDialogBuilder.createConfirmDialog(this, getString(R.string.discard_your_changes),
            getString(R.string.discard))
            .onNegative((dialog, which) -> dialog.dismiss())
            .onPositive((dialog, which) -> {
                dialog.dismiss();
                finish();
            })
            .show();
    }

    private void checkDiscardConfirmRequirement() {
        if (selectedNews != null) {
            if (isModified()) {
                showDiscardConfirm();
            } else {
                finish();
            }
        } else {
            if (!"".equals(title.getText().toString())
                || !"".equals(description.getText().toString())
                || thumbnailUri != null
                || attachmentAdapter.hasAttachment()) {
                showDiscardConfirm();
            } else {
                finish();
            }
        }
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

    @OnClick(R.id.thumbnailContainer)
    protected void thumbnailContainerClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_THUMBNAIL, thumbnailUri != null);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(KEY_SAVED_THUMBNAIL_URI)) {
            thumbnailUri = Uri.parse(savedInstanceState.getString(KEY_SAVED_THUMBNAIL_URI));
            Glide.with(this).load(thumbnailUri).into(thumbnail);
        }
        if (savedInstanceState.containsKey(KEY_SAVED_ATTACHMENTS)) {
            String dataInJson = savedInstanceState.getString(KEY_SAVED_ATTACHMENTS);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Attachment>>() {
            }.getType();
            List<Attachment> savedData = gson.fromJson(dataInJson, type);
            if (attachmentAdapter != null) {
                attachmentAdapter.setData(savedData);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (thumbnailUri != null) {
            outState.putString(KEY_SAVED_THUMBNAIL_URI, thumbnailUri.toString());
        }
        if (!attachmentAdapter.getData().isEmpty()) {
            Gson gson = new Gson();
            String dataInJson = gson.toJson(attachmentAdapter.getData());
            outState.putString(KEY_SAVED_ATTACHMENTS, dataInJson);
        }
        super.onSaveInstanceState(outState);
    }
}
