package com.dat.barnaulzoopark.ui.newseditor;

import android.net.Uri;
import com.dat.barnaulzoopark.model.Attachment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

public interface NewsItemEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<NewsItemEditorContract.View> {

        void updateOrCreateNewsItem(String title, String description, Uri thumbnailUri,
            List<Attachment> attachments);
    }
}
