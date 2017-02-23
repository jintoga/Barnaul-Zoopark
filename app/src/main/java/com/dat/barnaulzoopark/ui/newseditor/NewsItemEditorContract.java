package com.dat.barnaulzoopark.ui.newseditor;

import android.net.Uri;
import android.support.annotation.NonNull;
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

        void onDeleteImageError(@NonNull String errorMsg);

        void onDeleteImageSuccessful();

        void onUploadFailure(@NonNull String errorMsg);

        void onUploadSuccess();

        void showUploadNewsItemProgress();

        void showUploadThumbnailProgress();

        void showDeleteImageProgress();
    }

    interface UserActionListener extends MvpPresenter<NewsItemEditorContract.View> {

        void loadSelectedNews(String selectedNewsUid);

        void createNewsItem(String title, String description, Uri thumbnailUri,
            List<Attachment> attachments);
    }
}
