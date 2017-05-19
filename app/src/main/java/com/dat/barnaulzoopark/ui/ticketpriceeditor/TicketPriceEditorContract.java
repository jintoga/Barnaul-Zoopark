package com.dat.barnaulzoopark.ui.ticketpriceeditor;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by DAT on 5/20/2017.
 */

interface TicketPriceEditorContract {
    interface View extends MvpView {
    }

    interface UserActionListener extends MvpPresenter<TicketPriceEditorContract.View> {
    }
}
