package com.dat.barnaulzoopark.ui.ticketprice;

import com.google.firebase.database.DatabaseReference;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/20/2017.
 */

interface TicketPriceContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<TicketPriceContract.View> {
        DatabaseReference getTicketPricesReference();
    }
}
