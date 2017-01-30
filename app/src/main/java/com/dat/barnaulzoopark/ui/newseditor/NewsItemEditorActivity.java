package com.dat.barnaulzoopark.ui.newseditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorActivity extends
    BaseMvpPhotoEditActivity<NewsItemEditorContract.View, NewsItemEditorContract.UserActionListener>
    implements NewsItemEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.Listener {

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

    private int counter = 0;
    private int currentAttachmentPosition = 0;
    private boolean isThumbnailRequest = false;

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
    public void onTakePhotoClicked() {
        Log.d(TAG, "onTakePhotoClicked");
    }

    @Override
    public void onChoosePhotoClicked() {
        Log.d(TAG, "onChoosePhotoClicked");
    }

    @Override
    public void onRemovedPhotoClicked() {
        Log.d(TAG, "onRemovedPhotoClicked");
    }

    @Override
    public void onResultUriSuccess(@NonNull Uri uri) {

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
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/+");
        startActivityForResult(intent, REQUEST_BROWSE_IMAGE);
    }

    @OnClick(R.id.thumbnailContainer)
    protected void thumbnailContainerClicked() {
        createChangePhotoDialog();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BROWSE_IMAGE && data != null) {
            CropImage.activity(data.getData())
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (isThumbnailRequest) {
                    isThumbnailRequest = false;
                    Glide.with(this).load(resultUri).into(thumbnail);
                    currentPhoto = resultUri;
                    return;
                } else {
                    counter++;
                    Attachment attachment = new Attachment(true, resultUri.getPath());
                    attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
                    attachmentAdapter.addEmptySlot();
                    album.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, error.getLocalizedMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
