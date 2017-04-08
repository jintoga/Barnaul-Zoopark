package com.dat.barnaulzoopark.ui.animals.animalsfragment;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.model.animal.Category;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 3/28/2017.
 */

interface AnimalsContract {
    interface View extends MvpView {
        void bindCategories(@NonNull List<Category> categories);
    }

    interface UserActionListener extends MvpPresenter<View> {
        void loadCategories();
    }
}
