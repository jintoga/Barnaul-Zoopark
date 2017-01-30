package com.dat.barnaulzoopark.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.R;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by DAT on 1/30/2017.
 */

public abstract class BaseMvpPhotoEditActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends BaseMvpActivity<V, P> {

    protected static final int REQUEST_BROWSE_IMAGE = 1;
    private final String TAG = getClass().getName();

    protected Uri oldPhoto, currentPhoto;

    private Listener listener;

    public interface Listener {
        void onTakePhotoClicked();

        void onChoosePhotoClicked();

        void onRemovedPhotoClicked();

        void onResultUriSuccess(@NonNull Uri uri);
    }

    protected void setListener(Listener listener) {
        this.listener = listener;
    }

    protected void createChangePhotoDialog() {
        final MaterialDialog dialog = BZDialogBuilder.createChangePhotoDialog(this);
        View rootView = dialog.getCustomView();
        if (rootView == null) {
            return;
        }
        TextView removePhoto = (TextView) rootView.findViewById(R.id.removePhoto);
        TextView takePhoto = (TextView) rootView.findViewById(R.id.takePhoto);
        TextView choosePhoto = (TextView) rootView.findViewById(R.id.choosePhoto);
        if (oldPhoto == null && currentPhoto == null) {
            removePhoto.setVisibility(View.GONE);
        }
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRemovedPhotoClicked();
                }
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTakePhotoClicked();
                }
                dialog.dismiss();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onChoosePhotoClicked();
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/+");
                startActivityForResult(intent, REQUEST_BROWSE_IMAGE);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BROWSE_IMAGE && data != null) {
            CropImage.activity(data.getData())
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (listener != null) {
                    listener.onResultUriSuccess(resultUri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, error.getLocalizedMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
