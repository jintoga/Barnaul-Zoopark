package com.dat.barnaulzoopark.ui.bloganimal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.events.AnimalSubscribeEvent;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.ui.EventMvpPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by DAT on 5/13/2017.
 */

class BlogAnimalPresenter extends EventMvpPresenter<BlogAnimalContract.View>
    implements BlogAnimalContract.UserActionListener {

    private FirebaseDatabase database;

    BlogAnimalPresenter(@Nullable EventBus eventBus, FirebaseDatabase database) {
        super(eventBus);
        this.database = database;
    }

    @Override
    public void loadBlogs(@NonNull Set<String> animalUids) {
        if (getView() != null) {
            getView().onLoadBlogsProgress();
        }
        database.getReference()
            .child(BZFireBaseApi.blog_animal)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<BlogAnimal> blogs = new ArrayList<>();
                    List<BlogAnimal> result = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BlogAnimal blogAnimal = snapshot.getValue(BlogAnimal.class);
                        blogs.add(blogAnimal);
                    }
                    for (BlogAnimal blogAnimal : blogs) {
                        if (animalUids.contains(blogAnimal.getAnimalUid())) {
                            result.add(blogAnimal);
                        }
                    }
                    if (getView() != null) {
                        getView().bindBlogs(result);
                        getView().onLoadBlogsSuccess();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (getView() != null) {
                        getView().onLoadBlogsError(databaseError.getMessage());
                    }
                }
            });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AnimalSubscribeEvent event) {
        if (!isViewAttached()) {
            return;
        }
        if (getView() != null) {
            getView().updateBlogs();
        }
    }
}
