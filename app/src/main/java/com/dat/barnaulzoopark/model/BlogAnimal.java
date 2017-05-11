package com.dat.barnaulzoopark.model;

import android.support.annotation.NonNull;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 5/12/2017.
 */
@IgnoreExtraProperties
public class BlogAnimal {
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
}
