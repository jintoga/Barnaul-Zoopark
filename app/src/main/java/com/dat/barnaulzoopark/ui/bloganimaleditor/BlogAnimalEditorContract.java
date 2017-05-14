package com.dat.barnaulzoopark.ui.bloganimaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 5/13/2017.
 */

public interface BlogAnimalEditorContract {
    interface View extends MvpView {
        void bindAnimals(@NonNull List<Animal> animals);

        void onCreatingFailure(@NonNull String msg);

        void onLoadAnimalsError(@NonNull String msg);
    }

    interface UserActionListener extends MvpPresenter<View> {
        void createAnimal(@NonNull String title, @NonNull String description,
            @NonNull String animalUid, @Nullable Uri iconUri, @NonNull List<Attachment> attachments,
            @NonNull String videoUrl);

        void loadAnimals();

        void loadSelectedBlog(@NonNull String selectedBlogUid);
    }
}
