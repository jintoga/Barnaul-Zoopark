package com.dat.barnaulzoopark.model;

import android.support.annotation.NonNull;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 5/21/2017.
 */

public class PhotoAlbum extends AbstractData {
    private String uid;
    private String name;
    private Long time;
    private Map<String, String> photos;

    public PhotoAlbum() {
    }

    public PhotoAlbum(String uid, String name, Long time) {
        this.uid = uid;
        this.name = name;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Long getTime() {
        return time;
    }

    @NonNull
    public Map<String, String> getPhotos() {
        return photos == null ? photos = new HashMap<>() : photos;
    }

    @Exclude
    @Override
    public String getPhotoUrl() {
        return null;
    }

    @Exclude
    @Override
    public String getText() {
        return getName();
    }

    @Exclude
    @Override
    public String getId() {
        return getUid();
    }
}
