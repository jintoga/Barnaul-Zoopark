package com.dat.barnaulzoopark.ui.ticketprice;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 5/20/2017.
 */

class TicketPricePresenter extends MvpBasePresenter<TicketPriceContract.View>
    implements TicketPriceContract.UserActionListener {

    private FirebaseDatabase database;

    TicketPricePresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public DatabaseReference getTicketPricesReference() {
        return database.getReference().child(BZFireBaseApi.ticket_price);
    }
}
