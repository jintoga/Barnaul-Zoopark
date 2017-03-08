package com.dat.barnaulzoopark.ui.newseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.News;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

public interface NewsItemEditorContract {
    interface View extends MvpView {
        void bindSelectedNews(@NonNull News selectedNews);

        void highlightRequiredFields();

        void deletingNewsItem();

        void onDeleteNewsItemFailure(@NonNull String errorMsg);

        void onDeleteNewsItemSuccessful();

        void onUpdatingComplete();

        void onUpdatingNewsFailure(@NonNull String errorMsg);

        void onUpdatingNewsSuccess();

        void onUploadFailure(@NonNull String errorMsg);

        void onCreatingComplete();

        void creatingNewsItemProgress();

        void onCreatingNewsItemSuccess();

        void onCreatingNewsItemFailure(@NonNull String errorMsg);

        void updatingNewsItemProgress();

        void uploadingAttachments();

        void uploadingThumbnailProgress();
    }

    interface UserActionListener extends MvpPresenter<NewsItemEditorContract.View> {

        void deleteNewsItem(@Nullable String selectedNewsUid);

        void loadSelectedNews(@NonNull String selectedNewsUid);

        void createNewsItem(@NonNull String title, @NonNull String description,
            @Nullable Uri thumbnailUri, @NonNull List<Attachment> attachments,
            @NonNull String video);

        void updateSelectedNewsItem(@NonNull News selectedNews, @NonNull String title,
            @NonNull String description, @Nullable Uri thumbnailUri,
            @NonNull List<Attachment> itemsToAdd, @NonNull List<Attachment> itemsToDelete,
            @NonNull String video);
    }
}
