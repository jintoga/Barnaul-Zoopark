package com.dat.barnaulzoopark.ui.newsdetail;

import android.support.annotation.NonNull;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by DAT on 1/9/2017.
 */

class NewsDetailPresenter extends MvpBasePresenter<NewsDetailContract.View>
    implements NewsDetailContract.UserActionListener {

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    NewsDetailPresenter(FirebaseDatabase database, FirebaseStorage storage) {
        this.database = database;
        this.storage = storage;
    }

    @Override
    public void loadNewsDetail(@NonNull String newsUid) {
        DatabaseReference databaseReference =
            database.getReference().child(BZFireBaseApi.news).child(newsUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                if (getView() != null) {
                    getView().showNews(news);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
