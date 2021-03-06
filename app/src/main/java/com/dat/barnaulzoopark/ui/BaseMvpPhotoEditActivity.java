package com.dat.barnaulzoopark.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BuildConfig;
import com.dat.barnaulzoopark.R;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;

/**
 * Created by DAT on 1/30/2017.
 */

public abstract class BaseMvpPhotoEditActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends BaseMvpActivity<V, P> {

    private static final String TAG = BaseMvpPhotoEditActivity.class.getName();

    private int originalRequestCode;
    private boolean isCapturePhoto = false;
    private static final int REQUEST_CODE_PERMISSIONS = 200;
    private PhotoEditListener photoEditListener;
    private static final String TEMP_IMAGE_NAME = "temporary_image";

    public interface PhotoEditListener {
        void onRemovedPhotoClicked(int requestCode);

        void onResultUriSuccess(@NonNull Uri uri, int originalRequestCode);

        void onCropError(@NonNull String errorMsg);
    }

    protected void setPhotoEditListener(PhotoEditListener photoEditListener) {
        this.photoEditListener = photoEditListener;
    }

    protected void createChangePhotoDialog(final int requestCode) {
        createChangePhotoDialog(requestCode, false);
    }

    protected void createChangePhotoDialog(final int requestCode, boolean isFilledWithPhoto) {
        originalRequestCode = requestCode;
        final MaterialDialog dialog = BZDialogBuilder.createChangePhotoDialog(this);
        View rootView = dialog.getCustomView();
        if (rootView == null) {
            return;
        }
        TextView removePhoto = (TextView) rootView.findViewById(R.id.removePhoto);
        TextView takePhoto = (TextView) rootView.findViewById(R.id.takePhoto);
        final TextView choosePhoto = (TextView) rootView.findViewById(R.id.choosePhoto);
        if (isFilledWithPhoto) {
            removePhoto.setVisibility(View.VISIBLE);
        } else {
            removePhoto.setVisibility(View.GONE);
        }
        removePhoto.setOnClickListener(view -> {
            dialog.dismiss();
            if (photoEditListener != null) {
                photoEditListener.onRemovedPhotoClicked(requestCode);
            }
        });
        takePhoto.setOnClickListener(view -> {
            isCapturePhoto = true;
            dialog.dismiss();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(BaseMvpPhotoEditActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BaseMvpPhotoEditActivity.this,
                        new String[] { Manifest.permission.CAMERA }, REQUEST_CODE_PERMISSIONS);
                    return;
                }
            }
            startCapturePhoto(requestCode);
        });
        choosePhoto.setOnClickListener(view -> {
            isCapturePhoto = false;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/+");
            startActivityForResult(intent, requestCode);
            dialog.dismiss();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == originalRequestCode && resultCode == RESULT_OK) {
            Uri uri = null;
            if (isCapturePhoto) {
                uri = Uri.fromFile(getTempFile());
            } else if (data != null) {
                uri = data.getData();
            }
            if (uri != null) {
                CropImage.activity(uri)
                    .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result != null) {
                Uri resultUri = result.getUri();
                if (photoEditListener != null) {
                    photoEditListener.onResultUriSuccess(resultUri, originalRequestCode);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
                && result != null) {
                Exception error = result.getError();
                Log.e(TAG, error.getLocalizedMessage());
                if (photoEditListener != null) {
                    photoEditListener.onCropError(error.getLocalizedMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        switch (permsRequestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCapturePhoto(originalRequestCode);
                } else {
                    Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startCapturePhoto(int requestCode) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                getTempFile());
        } else {
            photoURI = Uri.fromFile(getTempFile());
        }
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePhotoIntent, requestCode);
    }

    private File getTempFile() {
        File imageFile = new File(this.getExternalCacheDir(), TEMP_IMAGE_NAME);
        boolean directoryCreated = imageFile.getParentFile().mkdirs();
        if (!directoryCreated && !imageFile.getParentFile().exists()) {
            Log.i(TAG, "Failed to create directory for temp file:  " + imageFile.getParentFile());
        }
        return imageFile;
    }
}
