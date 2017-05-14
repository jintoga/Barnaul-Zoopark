package com.dat.barnaulzoopark.ui.bloganimaleditor;

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
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpPhotoEditActivity;
import com.dat.barnaulzoopark.ui.adapters.BaseHintSpinnerAdapter;
import com.dat.barnaulzoopark.ui.adapters.MultiFileAttachmentAdapter;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.MultiAttachmentDecoration;
import com.dat.barnaulzoopark.widget.PrefixEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by DAT on 5/13/2017.
 */

public class BlogAnimalEditorActivity extends
    BaseMvpPhotoEditActivity<BlogAnimalEditorContract.View, BlogAnimalEditorContract.UserActionListener>
    implements BlogAnimalEditorContract.View, MultiFileAttachmentAdapter.AttachmentListener,
    BaseMvpPhotoEditActivity.PhotoEditListener {

    private static final String TAG = BlogAnimalEditorActivity.class.getName();
    private static final String EXTRA_SELECTED_BLOG_UID = "EXTRA_SELECTED_BLOG_UID";

    private static final String KEY_SAVED_ATTACHMENTS = "SAVED_ATTACHMENTS";
    private static final String KEY_SAVED_THUMBNAIL_URI = "SAVED_THUMBNAIL_URI";
    private static final int REQUEST_BROWSE_IMAGE_THUMBNAIL = 1;
    private static final int REQUEST_BROWSE_IMAGE_ATTACHMENT = 2;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;
    @Bind(R.id.animals)
    protected Spinner animals;
    BaseHintSpinnerAdapter<Animal> animalsSpinnerAdapter;
    @Bind(R.id.title)
    protected EditText title;
    @Bind(R.id.description)
    protected EditText description;
    @Bind(R.id.album)
    protected RecyclerView album;
    private MultiFileAttachmentAdapter attachmentAdapter;
    @Bind(R.id.video)
    protected PrefixEditText video;

    private int filledAttachmentCounter = 0;
    private int currentAttachmentPosition = 0;

    private Uri thumbnailUri;

    private MaterialDialog progressDialog;

    private BlogAnimal selectedBlog;

    public static void start(@NonNull Context context, @Nullable String blogUid) {
        if (context instanceof BlogAnimalEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, BlogAnimalEditorActivity.class);
        if (blogUid != null) {
            intent.putExtra(EXTRA_SELECTED_BLOG_UID, blogUid);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_animal_editor);
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
        initAttachmentsRecyclerView();
        presenter.loadAnimals();
        String selectedBlogUid = getIntent().getStringExtra(EXTRA_SELECTED_BLOG_UID);
        if (selectedBlogUid != null) {
            presenter.loadSelectedBlog(selectedBlogUid);
            attachmentAdapter.setEditingMode(true);
            updateTitle(getString(R.string.edit_blog_animal));
        } else {
            updateTitle(getString(R.string.create_blog_animal));
        }
    }

    private void initAttachmentsRecyclerView() {
        album.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        album.addItemDecoration(new MultiAttachmentDecoration(
            (int) getResources().getDimension(R.dimen.item_file_attachment_decoration)));
        attachmentAdapter = new MultiFileAttachmentAdapter(this);
        album.setAdapter(attachmentAdapter);
        attachmentAdapter.addEmptySlot();

        video.setPrefix(getString(R.string.youtube_prefix));
    }

    private void updateTitle(@NonNull String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @NonNull
    @Override
    public BlogAnimalEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new BlogAnimalEditorPresenter(database, storage);
    }

    @OnClick(R.id.thumbnailContainer)
    protected void thumbnailContainerClicked() {
        createChangePhotoDialog(REQUEST_BROWSE_IMAGE_THUMBNAIL, thumbnailUri != null);
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

    @Override
    public void onRemovedPhotoClicked(int requestCode) {
        Log.d(TAG, "onRemovedPhotoClicked");
        thumbnailUri = null;
        thumbnail.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.img_photo_gallery_placeholder));
    }

    @Override
    public void onCropError(@NonNull String errorMsg) {
        showSnackBar(errorMsg);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_animal_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //ToDo: implement discard dialog
                finish();
                break;
            case R.id.save:
                if (selectedBlog == null) {
                    createBlogAnimal();
                } else {
                    editBlogAnimal();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createBlogAnimal() {
        if (getAnimalUid() != null) {
            presenter.createAnimal(title.getText().toString(), description.getText().toString(),
                getAnimalUid(), thumbnailUri, attachmentAdapter.getData(),
                video.getText().toString());
        } else {
            onCreatingFailure(getString(R.string.animal_invalid_error));
        }
    }

    private void editBlogAnimal() {
        if (getAnimalUid() != null) {
            presenter.editAnimal(selectedBlog, title.getText().toString(),
                description.getText().toString(), getAnimalUid(), thumbnailUri,
                attachmentAdapter.getItemsToAdd(), attachmentAdapter.getItemsToDelete(),
                video.getText().toString());
        } else {
            onEditError(getString(R.string.species_invalid_error));
        }
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> animals) {
        initSpinner(animals);
    }

    @Override
    public void bindSelectedAnimal(@NonNull BlogAnimal blogAnimal) {
        this.selectedBlog = blogAnimal;
        for (int i = 1; i < animalsSpinnerAdapter.getData().size();
            i++) { //position 0 is hint so start from 1
            if (animalsSpinnerAdapter.getData()
                .get(i)
                .getUid()
                .equals(selectedBlog.getAnimalUid())) {
                animals.setSelection(i);
                break;
            }
        }
        title.setText(selectedBlog.getTitle());
        description.setText(selectedBlog.getDescription());
        if (selectedBlog.getVideo() != null) {
            video.setText(selectedBlog.getVideo());
        }
        if (selectedBlog.getThumbnail() != null) {
            thumbnailUri = Uri.parse(selectedBlog.getThumbnail());
            Glide.with(this).load(thumbnailUri).into(thumbnail);
        }
        for (Map.Entry<String, String> entry : selectedBlog.getPhotos().entrySet()) {
            filledAttachmentCounter++;
            Attachment attachment = new Attachment(true, entry.getValue());
            attachment.setAttachmentUid(entry.getKey());
            attachmentAdapter.fillSlot(currentAttachmentPosition, attachment);
            attachmentAdapter.addEmptySlot();
            album.smoothScrollToPosition(attachmentAdapter.getItemCount() - 1);
            currentAttachmentPosition++;
        }
    }

    @Override
    public void creatingProgress() {
        Log.d(TAG, "creatingProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.creating_blog_animal));
        }
        progressDialog.setContent(getString(R.string.creating_blog_animal));
        progressDialog.show();
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

    private void initSpinner(@NonNull List<Animal> data) {
        animalsSpinnerAdapter = new BaseHintSpinnerAdapter<Animal>(this, data) {
            @Override
            protected String getItemStringValue(@NonNull Animal animal) {
                return animal.getName();
            }
        };
        animalsSpinnerAdapter.setHint(getString(R.string.select_animal_hint));
        animals.setAdapter(animalsSpinnerAdapter);
    }

    @Override
    public void onCreatingFailure(@NonNull String msg) {
        Log.d(TAG, "onCreatingFailure " + msg);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    @Override
    public void onCreatingSuccess() {
        Log.d(TAG, "onCreatingSuccess");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showToast(getString(R.string.blog_animal_created_success));
        finish();
    }

    @Override
    public void onLoadAnimalsError(@NonNull String msg) {
        Log.d(TAG, "onLoadAnimalsError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(msg);
    }

    @Override
    public void onLoadBlogError(@NonNull String localizedMessage) {
        Log.d(TAG, "onLoadBlogError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void onLoadBlogProgress() {
        Log.d(TAG, "onLoadBlogProgress");
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.loading_selected_blog));
        }
        progressDialog.setContent(getString(R.string.loading_selected_blog));
        progressDialog.show();
    }

    @Override
    public void onLoadBlogSuccess() {
        Log.d(TAG, "onLoadBlogSuccess");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showEditingProgress() {
        Log.d(TAG, "showEditingProgress");
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, getString(R.string.updating_blog));
        }
        progressDialog.setContent(getString(R.string.updating_blog));
        progressDialog.show();
    }

    @Override
    public void onEditSuccess() {
        Log.d(TAG, "onEditSuccess");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, R.string.edit_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onEditError(@NonNull String localizedMessage) {
        Log.d(TAG, "onEditError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Nullable
    public String getAnimalUid() {
        if (animals.getSelectedItemPosition() > 0) {
            Animal selectedAnimal = (Animal) animals.getSelectedItem();
            return selectedAnimal.getUid();
        }
        return null;
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
