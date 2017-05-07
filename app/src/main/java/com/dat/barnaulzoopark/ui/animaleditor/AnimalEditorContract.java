package com.dat.barnaulzoopark.ui.animaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.google.firebase.database.DatabaseReference;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

/**
 * Created by DAT on 3/11/2017.
 */

public interface AnimalEditorContract {
    interface View extends MvpView {
        void creatingProgress();

        void highlightRequiredFields();
    }

    interface UserActionListener extends MvpPresenter<AnimalEditorContract.View> {

        void createAnimal(@NonNull String name, @NonNull String aboutAnimal,
            @NonNull String speciesUid, int genderSelectedItem, @Nullable Uri thumbnailUri,
            @NonNull List<Attachment> data, @NonNull String videoUrl);

        @NonNull
        DatabaseReference getSpeciesReference();
    }
}
