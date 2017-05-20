package com.dat.barnaulzoopark.ui.ticketpriceeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/20/2017.
 */

interface TicketPriceEditorContract {
    interface View extends MvpView {
        void highlightRequiredFields();

        void onCreatingFailure(@NonNull String localizedMessage);

        void onCreatingSuccess();

        void showCreatingProgress();
    }

    interface UserActionListener extends MvpPresenter<TicketPriceEditorContract.View> {
        void createTicketPrice(@NonNull String name, @NonNull String price, @Nullable Uri iconUri);
    }
}
