package com.dat.barnaulzoopark.ui.ticketpriceeditor;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 5/20/2017.
 */

class TicketPriceEditorPresenter extends MvpBasePresenter<TicketPriceEditorContract.View>
    implements TicketPriceEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    TicketPriceEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }
}
