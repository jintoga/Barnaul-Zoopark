package com.dat.barnaulzoopark.ui.videoalbumeditor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbumEditorPresenter extends MvpBasePresenter<VideoAlbumEditorContract.View>
    implements VideoAlbumEditorContract.UserActionListener {
    private FirebaseDatabase database;

    VideoAlbumEditorPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void createVideoAlbum(@NonNull String name, @Nullable Date date,
        @NonNull List<Attachment> data) {

    }

    @Override
    public void editVideoAlbum(VideoAlbum selectedVideoAlbum, @NonNull String name,
        @Nullable Date date, @NonNull List<Attachment> attachmentsToAdd,
        @NonNull List<Attachment> attachmentsToDelete) {

    }

    @Override
    public void loadSelectedVideoAlbum(@NonNull String selectedVideoAlbumUid) {

    }
}
