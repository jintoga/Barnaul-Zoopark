package com.dat.barnaulzoopark.ui.photoalbumeditor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 5/21/2017.
 */

interface PhotoAlbumEditorContract {
    interface View extends MvpView {
        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String localizedMessage);

        void onCreatingSuccess();

        void showCreatingProgress();
    }

    interface UserActionListener extends MvpPresenter<PhotoAlbumEditorContract.View> {
        void createPhotoAlbum(@NonNull String name, @Nullable Date date,
            @NonNull List<Attachment> data);

        void loadSelectedPhotoAlbum(@NonNull String selectedPhotAlbumUid);
    }
}
