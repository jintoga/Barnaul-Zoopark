package com.dat.barnaulzoopark.ui.newseditor;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorPresenter extends MvpBasePresenter<NewsItemEditorContract.View>
    implements NewsItemEditorContract.UserActionListener {
    private static final String TAG = NewsItemEditorPresenter.class.getName();
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    public NewsItemEditorPresenter(@NonNull Context context, FirebaseDatabase database,
        FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void updateOrCreateNewsItem(@NonNull String title, @NonNull String description,
        @Nullable Uri thumbnailUri, @NonNull List<Attachment> attachments) {

    }
}
