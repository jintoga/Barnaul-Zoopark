package com.dat.barnaulzoopark.ui.photoalbumeditor;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/21/2017.
 */

interface PhotoAlbumEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<PhotoAlbumEditorContract.View> {
        void loadSelectedPhotoAlbum(@NonNull String selectedPhotAlbumUid);
    }
}
