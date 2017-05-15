package com.dat.barnaulzoopark.ui.bloganimaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.BlogAnimal;
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

        void bindSelectedAnimal(@NonNull BlogAnimal blogAnimal);

        void creatingProgress();

        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String msg);

        void onCreatingSuccess();

        void onLoadAnimalsError(@NonNull String msg);

        void onLoadBlogError(@NonNull String localizedMessage);

        void onLoadBlogProgress();

        void onLoadBlogSuccess();

        void showEditingProgress();

        void onEditSuccess();

        void onEditError(@NonNull String localizedMessage);
    }

    interface UserActionListener extends MvpPresenter<View> {
        void createBlogAnimal(@NonNull String title, @NonNull String description,
            @NonNull String blogAnimalUid, @Nullable Uri thumbnailUri,
            @NonNull List<Attachment> attachments, @NonNull String videoUrl);

        void editBlogAnimal(@NonNull BlogAnimal selectedBlog, @NonNull String title,
            @NonNull String description, @NonNull String blogAnimalUid, @Nullable Uri thumbnailUri,
            @NonNull List<Attachment> attachmentsToAdd,
            @NonNull List<Attachment> attachmentsToDelete, @NonNull String videoUrl);

        void loadAnimals();

        void loadSelectedBlog(@NonNull String selectedBlogUid);
    }
}
