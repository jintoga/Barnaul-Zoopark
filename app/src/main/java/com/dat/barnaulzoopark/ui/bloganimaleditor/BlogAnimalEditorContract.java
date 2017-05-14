package com.dat.barnaulzoopark.ui.bloganimaleditor;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/13/2017.
 */

public interface BlogAnimalEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<View> {
        void loadAnimals();

        void loadSelectedBlog(@NonNull String selectedBlogUid);
    }
}
