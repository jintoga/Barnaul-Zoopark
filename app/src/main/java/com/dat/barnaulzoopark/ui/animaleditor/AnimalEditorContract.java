package com.dat.barnaulzoopark.ui.animaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.DatabaseReference;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 3/11/2017.
 */

public interface AnimalEditorContract {
    interface View extends MvpView {

        void bindSpecies(@NonNull List<Species> speciesList);

        void onLoadSpeciesError(@NonNull String message);

        void creatingProgress();

        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String msg);

        void onCreatingSuccess();

        void onLoadAnimalError(@NonNull String localizedMessage);

        void onLoadAnimalSuccess();

        void showLoadingProgress();

        void bindSelectedAnimal(@NonNull Animal selectedAnimal);
    }

    interface UserActionListener extends MvpPresenter<AnimalEditorContract.View> {

        void createAnimal(@NonNull String name, @NonNull String aboutAnimal,
            @NonNull String speciesUid, boolean gender, @Nullable Date dateOfBirth,
            @Nullable Uri iconUri, @Nullable Uri bannerImageUri, @Nullable Uri habitatMapImageUri,
            @NonNull List<Attachment> data, @NonNull String videoUrl);

        @NonNull
        DatabaseReference getSpeciesReference();

        void loadSelectedAnimal(@NonNull String selectedAnimalUid);

        void loadSpecies();
    }
}
