package com.dat.barnaulzoopark.ui.videoalbumeditor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

interface VideoAlbumEditorContract {
    interface View extends MvpView {

        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String localizedMessage);

        void onCreatingSuccess();

        void onEditError(@NonNull String localizedMessage);

        void onEditSuccess();

        void showCreatingProgress();

        void showEditingProgress();

        void showLoadingProgress();

        void bindSelectedVideoAlbum(@NonNull VideoAlbum videoAlbum);

        void onLoadError(@NonNull String localizedMessage);

        void onLoadSuccess();
    }

    interface UserActionListener extends MvpPresenter<VideoAlbumEditorContract.View> {
        void createVideoAlbum(@NonNull String name, @Nullable Date date,
            @NonNull List<Attachment> data);

        void editVideoAlbum(VideoAlbum selectedVideoAlbum, @NonNull String name,
            @Nullable Date date, @NonNull List<Attachment> attachmentsToAdd,
            @NonNull List<Attachment> attachmentsToDelete);

        void loadSelectedVideoAlbum(@NonNull String selectedVideoAlbumUid);
    }
}
