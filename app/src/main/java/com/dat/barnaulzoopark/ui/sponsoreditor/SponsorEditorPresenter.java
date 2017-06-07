package com.dat.barnaulzoopark.ui.sponsoreditor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.utils.UriUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import rx.Observable;

/**
 * Created by DAT on 6/4/2017.
 */

class SponsorEditorPresenter extends MvpBasePresenter<SponsorEditorContract.View>
    implements SponsorEditorContract.UserActionListener {
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    SponsorEditorPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void createSponsor(@NonNull String name, @NonNull String website,
        @Nullable Uri iconUri) {
        if (!"".equals(name)) {
            create(name, website, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    @Override
    public void editSponsor(@NonNull Sponsor selectedSponsor, @NonNull String name,
        @NonNull String website, @Nullable Uri iconUri) {
        if (!"".equals(name)) {
            edit(selectedSponsor, name, website, iconUri);
        } else {
            if (getView() != null) {
                getView().highlightRequiredFields();
            }
        }
    }

    private void edit(@NonNull Sponsor selectedSponsor, @NonNull String name,
        @NonNull String website, @Nullable Uri iconUri) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.sponsors);
        final DatabaseReference itemReference = databaseReference.child(selectedSponsor.getUid());
        selectedSponsor.update(name);
        selectedSponsor.setSite(!website.isEmpty() ? website : null);
        itemReference.setValue(selectedSponsor);
        if (getView() != null) {
            getView().showEditingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(itemReference, Sponsor.class)
            .flatMap(category -> updateIcon(selectedSponsor, iconUri, itemReference))
            .doOnCompleted(() -> {
                if (getView() != null) {
                    getView().onEditSuccess();
                }
            })
            .doOnError(throwable -> {
                if (getView() != null) {
                    getView().onEditError(throwable.getLocalizedMessage());
                }
            })
            .subscribe();
    }

    @NonNull
    private Observable<Sponsor> updateIcon(@NonNull Sponsor selectedSponsor, @Nullable Uri iconUri,
        @NonNull DatabaseReference itemReference) {
        String filePath = BZFireBaseApi.sponsors + "/" + selectedSponsor.getUid() + "/" + "logo";
        if (iconUri == null && selectedSponsor.getLogo() != null) {
            //delete
            return deleteIcon(selectedSponsor, itemReference, filePath);
        } else if (iconUri != null && UriUtil.isLocalFile(iconUri)) {
            //update
            return uploadIcon(selectedSponsor, iconUri);
        } else {
            return Observable.just(selectedSponsor);
        }
    }

    @NonNull
    private Observable<Sponsor> deleteIcon(@NonNull Sponsor selectedSponsor,
        @NonNull DatabaseReference itemReference, @NonNull String filePath) {
        StorageReference storageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(storageReference).flatMap(aVoid -> {
            //set logo Value in selectedSponsor to null
            selectedSponsor.clearLogo();
            itemReference.setValue(selectedSponsor);
            return Observable.just(selectedSponsor);
        });
    }

    private void create(@NonNull String name, @NonNull String website, @Nullable Uri iconUri) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.sponsors);
        final String uid = databaseReference.push().getKey();
        final DatabaseReference itemReference = databaseReference.child(uid);
        Sponsor sponsor = new Sponsor(uid, name);
        if (!website.isEmpty()) {
            sponsor.setSite(website);
        }
        itemReference.setValue(sponsor);
        if (getView() != null) {
            getView().showCreatingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(itemReference, Sponsor.class)
            .flatMap(sponsor1 -> uploadIcon(sponsor1, iconUri))
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
    private Observable<Sponsor> uploadIcon(@NonNull Sponsor sponsor, @Nullable Uri iconUri) {
        if (iconUri == null) {
            return Observable.just(sponsor);
        } else {
            return getObservableUploadIcon(sponsor, iconUri);
        }
    }

    @NonNull
    private Observable<Sponsor> getObservableUploadIcon(@NonNull final Sponsor sponsor,
        @NonNull Uri uri) {
        String filePath = BZFireBaseApi.sponsors + "/" + sponsor.getUid() + "/" + "logo";
        return uploadImage(filePath, uri).flatMap(
            uploadedUri -> setLogoToSponsorPrice(sponsor, uploadedUri));
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
    private Observable<Sponsor> setLogoToSponsorPrice(@NonNull Sponsor sponsor,
        @NonNull Uri uploadedUri) {
        DatabaseReference databaseReference = database.getReference().child(BZFireBaseApi.sponsors);
        DatabaseReference itemReference = databaseReference.child(sponsor.getUid());
        itemReference.child("logo").setValue(uploadedUri.toString());
        return Observable.just(sponsor);
    }

    @Override
    public void loadSelectedSponsor(@NonNull String selectedSponsorUid) {
        DatabaseReference databaseReference =
            database.getReference(BZFireBaseApi.sponsors).child(selectedSponsorUid);
        if (getView() != null) {
            getView().showLoadingProgress();
        }
        RxFirebaseDatabase.observeSingleValueEvent(databaseReference, Sponsor.class)
            .subscribe(sponsor -> {
                if (getView() != null) {
                    getView().bindSelectedSponsor(sponsor);
                }
            }, throwable -> {
                if (getView() != null) {
                    getView().onLoadError(throwable.getLocalizedMessage());
                }
            }, () -> {
                if (getView() != null) {
                    getView().onLoadSuccess();
                }
            });
    }
}
