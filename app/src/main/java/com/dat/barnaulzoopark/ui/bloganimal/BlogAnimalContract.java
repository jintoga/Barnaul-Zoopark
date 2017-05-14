package com.dat.barnaulzoopark.ui.bloganimal;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import java.util.Set;

/**
 * Created by DAT on 5/13/2017.
 */

interface BlogAnimalContract {
    interface View extends MvpView {

        void bindBlogs(@NonNull List<BlogAnimal> result);

        void onLoadBlogsError(@NonNull String message);

        void onLoadBlogsProgress();

        void onLoadBlogsSuccess();

        void updateBlogs();
    }

    interface UserActionListener extends MvpPresenter<View> {
        void loadBlogs(@NonNull Set<String> animalUids);
    }
}
