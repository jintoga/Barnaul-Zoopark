package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 6/4/2017.
 */

interface SponsorEditorContract {
    interface View extends MvpView {

    }

    interface UserActionListener extends MvpPresenter<SponsorEditorContract.View> {
        void loadSelectedSponsor(@NonNull String selectedSponsorUid);
    }
}
