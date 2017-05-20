package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.AbstractData;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.model.TicketPrice;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementPresenter extends MvpBasePresenter<DataManagementContract.View>
    implements DataManagementContract.UserActionListener {

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    DataManagementPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public DatabaseReference getDataReference(String referenceName) {
        return database.getReference(referenceName);
    }

    @Override
    public void removeItem(AbstractData data) {
        remove(data);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractData> void remove(@NonNull final T data) {
        DatabaseReference databaseReference;
        Class clazz;
        if (data instanceof BlogAnimal) {
            databaseReference = database.getReference(BZFireBaseApi.blog_animal);
            clazz = BlogAnimal.class;
        } else if (data instanceof Animal) {
            databaseReference = database.getReference(BZFireBaseApi.animal);
            clazz = Animal.class;
        } else if (data instanceof Species) {
            databaseReference = database.getReference(BZFireBaseApi.animal_species);
            clazz = Species.class;
        } else if (data instanceof Category) {
            databaseReference = database.getReference(BZFireBaseApi.animal_categories);
            clazz = Category.class;
        } else if (data instanceof News) {
            databaseReference = database.getReference(BZFireBaseApi.news);
            clazz = News.class;
        } else if (data instanceof TicketPrice) {
            databaseReference = database.getReference(BZFireBaseApi.ticket_price);
            clazz = TicketPrice.class;
        } else {
            return;
        }
        final DatabaseReference childDatabaseReference = databaseReference.child(data.getId());
        if (getView() != null) {
            getView().showRemoveProgress();
        }
        final DatabaseReference finalDatabaseReference = databaseReference;
        RxFirebaseDatabase.observeSingleValueEvent(childDatabaseReference, clazz)
            .flatMap(o -> getDeleteIconObservable(data))
            .doOnNext((Action1<T>) this::deleteUidInChild)
            .subscribe(new Observer<T>() {
                @Override
                public void onCompleted() {
                    if (getView() != null) {
                        getView().showRemoveSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().onRemoveError(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onNext(T data) {
                    finalDatabaseReference.child(data.getId()).removeValue();
                }
            });
    }

    private <T extends AbstractData> void deleteUidInChild(@NonNull final T data) {
        DatabaseReference databaseReference;
        if (data instanceof Animal) {
            deleteAnimalUidFromUserSubscription((Animal) data);
            //Animal's child is Blog Animal
            databaseReference = database.getReference(BZFireBaseApi.blog_animal);
        } else if (data instanceof Species) {
            //Species's child is Animal
            databaseReference = database.getReference(BZFireBaseApi.animal);
        } else if (data instanceof Category) {
            //Category's child is Species
            databaseReference = database.getReference(BZFireBaseApi.animal_species);
        } else {
            return;
        }
        final DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    deleteChildUid(data, snapshot, finalDatabaseReference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void deleteAnimalUidFromUserSubscription(@NonNull Animal animal) {
        //Delete animal's uid in user's subscriptions
        DatabaseReference databaseReference = database.getReference(BZFireBaseApi.users);
        final DatabaseReference finalDatabaseReference = databaseReference;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getSubscribedAnimals().containsKey(animal.getUid())) {
                        user.getSubscribedAnimals().remove(animal.getUid());
                        finalDatabaseReference.child(user.getUid()).setValue(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private <T extends AbstractData> void deleteChildUid(T data, DataSnapshot snapshot,
        DatabaseReference finalDatabaseReference) {
        if (data instanceof Animal) {
            BlogAnimal blogAnimal = snapshot.getValue(BlogAnimal.class);
            if (blogAnimal.getAnimalUid() != null && blogAnimal.getAnimalUid()
                .equals(data.getId())) {
                blogAnimal.clearAnimalUid();
                finalDatabaseReference.child(blogAnimal.getUid()).setValue(blogAnimal);
            }
        } else if (data instanceof Species) {
            Animal animal = snapshot.getValue(Animal.class);
            if (animal.getSpeciesUid() != null && animal.getSpeciesUid().equals(data.getId())) {
                animal.clearSpeciesUid();
                finalDatabaseReference.child(animal.getUid()).setValue(animal);
            }
        } else if (data instanceof Category) {
            Species species = snapshot.getValue(Species.class);
            if (species.getCategoryUid() != null && species.getCategoryUid().equals(data.getId())) {
                species.clearCategoryUid();
                finalDatabaseReference.child(species.getUid()).setValue(species);
            }
        }
    }

    private <T extends AbstractData> Observable getDeleteIconObservable(@NonNull final T data) {
        String prefix;
        if (data instanceof News) {
            News news = (News) data;
            return getDeleteBlogOrNewsImagesObservable(news, news.getUid(), BZFireBaseApi.news,
                news.getThumbnail(), news.getPhotos());
        } else if (data instanceof BlogAnimal) {
            BlogAnimal blogAnimal = (BlogAnimal) data;
            return getDeleteBlogOrNewsImagesObservable(blogAnimal, blogAnimal.getUid(),
                BZFireBaseApi.blog_animal, blogAnimal.getThumbnail(), blogAnimal.getPhotos());
        } else if (data instanceof Animal) {
            return getDeleteAnimalImagesObservable((Animal) data);
        } else if (data instanceof Species) {
            prefix = BZFireBaseApi.animal_species;
        } else if (data instanceof Category) {
            prefix = BZFireBaseApi.animal_categories;
        } else {
            prefix = BZFireBaseApi.ticket_price;
        }
        String filePath = prefix + "/" + data.getId() + "/" + "icon";

        if (data.getPhotoUrl() == null || data.getPhotoUrl().isEmpty()) {
            return Observable.just(data);
        }
        return deleteFile(filePath).flatMap(new Func1<Void, Observable<T>>() {
            @Override
            public Observable<T> call(Void aVoid) {
                return Observable.just(data);
            }
        });
    }

    @NonNull
    private Observable<AbstractData> getDeleteBlogOrNewsImagesObservable(@NonNull AbstractData data,
        @NonNull String uid, @NonNull String prefix, @Nullable String singleImageUrl,
        @NonNull Map<String, String> photos) {
        final String filePath = prefix + "/" + uid + "/" + "thumbnail";
        List<Observable<Void>> observables = new ArrayList<>();
        if (singleImageUrl != null) {
            observables.add(deleteFile(filePath));
        }
        if (!photos.isEmpty()) {
            observables.add(deleteAttachmentsObservable(uid, photos, prefix));
        }
        return !observables.isEmpty() ? Observable.concat(observables)
            .flatMap(aVoid -> Observable.just(data)) : Observable.just(data);
    }

    @NonNull
    private Observable<Animal> getDeleteAnimalImagesObservable(@NonNull Animal animal) {
        final String prefix = BZFireBaseApi.animal + "/" + animal.getUid() + "/";
        List<Observable<Void>> observables = new ArrayList<>();
        if (animal.getPhotoSmall() != null) {
            observables.add(deleteFile(prefix + "photoSmall"));
        }
        if (animal.getPhotoBig() != null) {
            observables.add(deleteFile(prefix + "photoBig"));
        }
        if (animal.getImageHabitatMap() != null) {
            observables.add(deleteFile(prefix + "imageHabitatMap"));
        }
        if (!animal.getPhotos().isEmpty()) {
            observables.add(deleteAttachmentsObservable(animal.getUid(), animal.getPhotos(),
                BZFireBaseApi.animal));
        }
        return !observables.isEmpty() ? Observable.concat(observables)
            .flatMap(aVoid -> Observable.just(animal)) : Observable.just(animal);
    }

    @NonNull
    private Observable<Void> deleteAttachmentsObservable(@NonNull String uid,
        @NonNull Map<String, String> photos, @NonNull String prefix) {
        return Observable.from(photos.keySet()).flatMap(attachmentUid -> {
            final String attachmentPath = prefix + "/" + uid + "/photos/" + attachmentUid;
            return deleteFile(attachmentPath);
        });
    }

    @NonNull
    private Observable<Void> deleteFile(@NonNull String filePath) {
        StorageReference storageReference = storage.getReference().child(filePath);
        return RxFirebaseStorage.delete(storageReference);
    }
}
