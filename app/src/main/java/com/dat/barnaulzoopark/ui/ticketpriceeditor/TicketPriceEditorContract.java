package com.dat.barnaulzoopark.ui.ticketpriceeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.TicketPrice;
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

        void showEditingProgress();

        void onEditSuccess();

        void onEditError(@NonNull String localizedMessage);

        void showLoadingProgress();

        void onLoadTicketPriceError(@NonNull String localizedMessage);

        void onLoadTicketPriceSuccess();

        void bindSelectedTicketPrice(@NonNull TicketPrice ticketPrice);
    }

    interface UserActionListener extends MvpPresenter<TicketPriceEditorContract.View> {
        void createTicketPrice(@NonNull String name, @NonNull String price, @Nullable Uri iconUri);

        void editTicketPrice(@NonNull TicketPrice selectedTicketPrice, @NonNull String name,
            @NonNull String price, @Nullable Uri iconUri);

        void loadSelectedTicketPrice(@NonNull String selectedTicketPriceUid);
    }
}
