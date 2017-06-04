package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    }

    interface UserActionListener extends MvpPresenter<SponsorEditorContract.View> {
        void createSponsor(@NonNull String name, @NonNull String website, @Nullable Uri iconUri);

        void loadSelectedSponsor(@NonNull String selectedSponsorUid);
    }
}
