package com.dat.barnaulzoopark.ui.newseditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorActivity extends
    BaseMvpPhotoEditActivity<NewsItemEditorContract.View, NewsItemEditorContract.UserActionListener>
    implements NewsItemEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.Listener {

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

    public static void start(Context context) {
        if (context instanceof NewsItemEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, NewsItemEditorActivity.class);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public NewsItemEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
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
                Attachment attachment = new Attachment(true, uri.getPath());
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
        attachmentAdapter.addEmptySlot();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_item_editor, menu);
        return true;
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
        if (attachmentAdapter.getData() != null && !attachmentAdapter.getData().isEmpty()) {
            Gson gson = new Gson();
            String dataInJson = gson.toJson(attachmentAdapter.getData());
            outState.putString(KEY_SAVED_ATTACHMENTS, dataInJson);
        }
        super.onSaveInstanceState(outState);
    }
}
