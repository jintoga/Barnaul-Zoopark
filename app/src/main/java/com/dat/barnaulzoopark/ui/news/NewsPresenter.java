package com.dat.barnaulzoopark.ui.news;

import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/7/2017.
 */

class NewsPresenter extends MvpBasePresenter<NewsContract.View>
    implements NewsContract.UserActionListener {

    private static final String TAG = NewsPresenter.class.getName();

    private FirebaseDatabase database;

    NewsPresenter(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public DatabaseReference getNewsReference() {
        return database.getReference(BZFireBaseApi.news);
    }

    @Override
    public void loadData() {
        database.getReference(BZFireBaseApi.news)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (getView() != null) {
                        getView().bindNewsDetail();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
