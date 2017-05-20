package com.dat.barnaulzoopark.ui.ticketpriceeditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.TicketPrice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import rx.Observable;

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

    @Override
    public void createTicketPrice(@NonNull String name, @NonNull String priceText,
        @Nullable Uri iconUri) {
        if (!"".equals(name) && !"".equals(priceText)) {
            double price = Double.valueOf(priceText);
            create(name, price, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void create(@NonNull String name, double price, @Nullable Uri iconUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.ticket_price);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference tickerPriceReference = databaseReference.child(uid);
        TicketPrice ticketPrice = new TicketPrice(uid, name, price);
        tickerPriceReference.setValue(ticketPrice);
        if (getView() != null) {
            getView().showCreatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(tickerPriceReference, TicketPrice.class)
            .flatMap(ticketPrice1 -> uploadIcon(ticketPrice1, iconUri))
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onCreatingSuccess();
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onCreatingFailure(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }

    @NonNull
    private Observable<TicketPrice> uploadIcon(@NonNull TicketPrice ticketPrice,
        @Nullable Uri iconUri) {
        if (iconUri == null) {
            return Observable.just(ticketPrice);
        } else {
            return getObservableUploadIcon(ticketPrice, iconUri);
        }
    }

    @NonNull
    private Observable<TicketPrice> getObservableUploadIcon(@NonNull final TicketPrice ticketPrice,
        @NonNull Uri uri) {
        String filePath = BZFireBaseApi.ticket_price + "/" + ticketPrice.getUid() + "/" + "icon";
        return uploadImage(filePath, uri).flatMap(
            uploadedUri -> setIconToTicketPrice(ticketPrice, uploadedUri));
    }

    @NonNull
    private Observable<Uri> uploadImage(@NonNull final String fileName, @NonNull Uri uri) {
        StorageReference storageReference = storage.getReference().child(fileName);
        return RxFirebaseStorage.putFile(storageReference, uri).flatMap(taskSnapshot -> {
            Uri uploadedUri = taskSnapshot.getDownloadUrl();
            return Observable.just(uploadedUri);
        });
    }

    @NonNull
    private Observable<TicketPrice> setIconToTicketPrice(@NonNull TicketPrice ticketPrice,
        @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.ticket_price);
        DatabaseReference ticketPriceReference = databaseReference.child(ticketPrice.getUid());
        ticketPriceReference.child("icon").setValue(uploadedUri.toString());
        return Observable.just(ticketPrice);
    }
}
