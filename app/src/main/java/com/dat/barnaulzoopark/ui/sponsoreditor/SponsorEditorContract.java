package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.Sponsor;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 6/4/2017.
 */

interface SponsorEditorContract {
    interface View extends MvpView {
        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String localizedMessage);

        void onCreatingSuccess();

        void showCreatingProgress();

        void showEditingProgress();

        void onEditSuccess();

        void onEditError(@NonNull String localizedMessage);

        void showLoadingProgress();

        void onLoadError(@NonNull String localizedMessage);

        void onLoadSuccess();

        void bindSelectedSponsor(@NonNull Sponsor sponsor);
    }

    interface UserActionListener extends MvpPresenter<SponsorEditorContract.View> {
        void createSponsor(@NonNull String name, @NonNull String website, @Nullable Uri iconUri);

        void editSponsor(@NonNull Sponsor selectedSponsor, @NonNull String name,
            @NonNull String website, @Nullable Uri iconUri);

        void loadSelectedSponsor(@NonNull String selectedSponsorUid);
    }
}
