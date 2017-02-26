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
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorActivity extends
    BaseMvpPhotoEditActivity<NewsItemEditorContract.View, NewsItemEditorContract.UserActionListener>
    implements NewsItemEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.Listener {

    private static final String EXTAR_SELECTED_NEWS_UID = "SELECTED_NEWS_UID";
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

    private Uri thumbnailUri;

    private int counter = 0;
    private int currentAttachmentPosition = 0;

    private MaterialDialog progressDialog;

    private News selectedNews;

    public static void start(Context context, @Nullable String uid) {
        if (context instanceof NewsItemEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, NewsItemEditorActivity.class);
        if (uid != null) {
            intent.putExtra(EXTAR_SELECTED_NEWS_UID, uid);
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
    public void onRemovedPhotoClicked() {
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
                counter++;
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
    }

    @Override
    public void bindSelectedNews(@NonNull News selectedNews) {
        if (selectedNews.getThumbnail() != null && !"".equals(selectedNews.getThumbnail())) {
            setFilledWithPhoto(true);
        }
        this.selectedNews = selectedNews;
        if (selectedNews.getThumbnail() != null) {
            thumbnailUri = Uri.parse(selectedNews.getThumbnail());
        }
        title.setText(selectedNews.getTitle());
        description.setText(selectedNews.getDescription());
        if (selectedNews.getThumbnail() != null && !"".equals(selectedNews.getThumbnail())) {
            Glide.with(this).load(Uri.parse(selectedNews.getThumbnail())).into(thumbnail);
        }
        if (selectedNews.getPhotos() != null && !selectedNews.getPhotos().isEmpty()) {
            List<Attachment> attachments = new ArrayList<>();
            for (String url : selectedNews.getPhotos().values()) {
                Attachment attachment = new Attachment(true, url);
                attachments.add(attachment);
            }
            attachmentAdapter.setData(attachments);
        }
        attachmentAdapter.addEmptySlot();
        counter = attachmentAdapter.getItemCount();
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
    public void deletingNewsItem() {
        Log.d(TAG, "deletingNewsItem");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this, "Deleting News Item");
        }
    }

    @Override
    public void onDeleteNewsItemFailure(@NonNull String errorMsg) {
        Log.d(TAG, "onDeleteNewsItemFailure" + errorMsg);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteNewsItemSuccessful() {
        Log.d(TAG, "onDeleteNewsItemSuccessful");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();
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
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this, "Creating News Item");
        }
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
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatingNewsItemProgress() {
        Log.d(TAG, "updatingNewsItemProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this, "Updating News Item");
        }
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
        setListener(this);
    }

    private void init() {
        album.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        album.addItemDecoration(new MultiAttachmentDecoration(
            (int) getResources().getDimension(R.dimen.item_file_attachment_decoration)));
        attachmentAdapter = new MultiFileAttachmentAdapter(this);
        album.setAdapter(attachmentAdapter);
        String selectedNewsUid = getIntent().getStringExtra(EXTAR_SELECTED_NEWS_UID);
        if (selectedNewsUid != null) {
            loadSelectedNews(selectedNewsUid);
        } else {
            attachmentAdapter.addEmptySlot();
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
                        description.getText().toString(), thumbnailUri,
                        attachmentAdapter.getData());
                } else {
                    if (isModified()) {
                        Log.d(TAG, "UPDATING");
                        presenter.updateSelectedNewsItem(selectedNews, title.getText().toString(),
                            description.getText().toString(), thumbnailUri,
                            attachmentAdapter.getData());
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.delete:
                String selectedNewsUid = getIntent().getStringExtra(EXTAR_SELECTED_NEWS_UID);
                presenter.deleteNewsItem(selectedNewsUid);
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
        if (attachmentAdapter.isModified(selectedNews)) {
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
            getString(R.string.discard)).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                finish();
            }
        }).show();
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
        if (counter == MultiFileAttachmentAdapter.MAX_NUMBER_ATTACHMENT) {
            attachmentAdapter.emptySlot(position);
            attachmentAdapter.addEmptySlot();
        } else {
            attachmentAdapter.emptySlot(position);
        }
        counter--;
    }

    @Override
    public void onSlotSelected(int position) {
        if (counter == MultiFileAttachmentAdapter.MAX_NUMBER_ATTACHMENT) {
            Toast.makeText(this, "Выбрано максимальное количество файлов", Toast.LENGTH_LONG)
                .show();
            return;
        }
        currentAttachmentPosition = position;
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_ATTACHMENT, false);
    }

    @OnClick(R.id.thumbnailContainer)
    protected void thumbnailContainerClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_THUMBNAIL, true);
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
