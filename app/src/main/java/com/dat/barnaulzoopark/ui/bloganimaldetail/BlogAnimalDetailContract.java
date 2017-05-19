package com.dat.barnaulzoopark.ui.bloganimaldetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/15/2017.
 */

interface BlogAnimalDetailContract {
    interface View extends MvpView {
        void showBlogAnimal(@Nullable BlogAnimal blogAnimal);
    }

    interface UserActionListener extends MvpPresenter<BlogAnimalDetailContract.View> {
        void loadBlogAnimal(@NonNull String blogUid);
    }
}
