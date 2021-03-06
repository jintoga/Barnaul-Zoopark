package com.dat.barnaulzoopark.ui.animaleditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 3/11/2017.
 */

interface AnimalEditorContract {
    interface View extends MvpView {

        void bindSpecies(@NonNull List<Species> speciesList);

        void bindSponsors(@NonNull List<Sponsor> sponsors);

        void onLoadError(@NonNull String message);

        void creatingProgress();

        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String msg);

        void onCreatingSuccess();

        void onLoadAnimalError(@NonNull String localizedMessage);

        void onLoadAnimalSuccess();

        void showEditingProgress();

        void onEditError(@NonNull String localizedMessage);

        void onEditSuccess();

        void showLoadingProgress();

        void bindSelectedAnimal(@NonNull Animal selectedAnimal);
    }

    interface UserActionListener extends MvpPresenter<AnimalEditorContract.View> {

        void createAnimal(@NonNull String name, @NonNull String aboutAnimal,
            @NonNull String speciesUid, boolean gender, @Nullable Date dateOfBirth,
            @Nullable Uri iconUri, @Nullable Uri bannerImageUri, @Nullable Uri habitatMapImageUri,
            @NonNull List<Attachment> attachments, @NonNull String videoUrl, @Nullable Double lat,
            @Nullable Double lng, @Nullable List<String> animalSponsorUids);

        void editAnimal(@NonNull Animal selectedAnimal, @NonNull String name,
            @NonNull String aboutAnimal, @NonNull String speciesUid, boolean gender,
            @Nullable Date dateOfBirth, @Nullable Uri iconUri, @Nullable Uri bannerImageUri,
            @Nullable Uri habitatMapImageUri, @NonNull List<Attachment> attachmentsToAdd,
            @NonNull List<Attachment> attachmentsToDelete, @NonNull String videoUrl,
            @Nullable Double lat, @Nullable Double lng, @Nullable List<String> animalSponsorUids);

        void loadSelectedAnimal(@NonNull String selectedAnimalUid);

        void loadSpecies();

        void loadSponsors();
    }
}
