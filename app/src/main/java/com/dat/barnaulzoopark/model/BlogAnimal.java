package com.dat.barnaulzoopark.model;

import android.support.annotation.NonNull;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 5/12/2017.
 */
@IgnoreExtraProperties
public class BlogAnimal extends AbstractData {
    private String uid;
    private String animalUid;
    private String title;
    private String description;
    private String thumbnail;
    private long time;
    private Map<String, String> photos = new HashMap<>();
    private String video;

    public BlogAnimal() {
        // Default constructor required for calls to DataSnapshot.getValue(News.class)
    }

    public BlogAnimal(String uid, String animalUid, String title, String description, long time) {
        this.uid = uid;
        this.animalUid = animalUid;
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getAnimalUid() {
        return animalUid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public long getTime() {
        return time;
    }

    public String getVideo() {
        return video;
    }

    @NonNull
    public Map<String, String> getPhotos() {
        return photos == null ? photos = new HashMap<>() : photos;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Exclude
    @Override
    public String getPhotoUrl() {
        return getThumbnail();
    }

    @Exclude
    @Override
    public String getText() {
        return getTitle();
    }

    @Exclude
    @Override
    public String getId() {
        return getUid();
    }
}
