package com.dat.barnaulzoopark.model;

import android.support.annotation.NonNull;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbum extends AbstractData {
    private String uid;
    private String name;
    private Long time;
    private Map<String, String> videos;

    public VideoAlbum() {
    }

    public VideoAlbum(String uid, String name, Long time) {
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
    public Map<String, String> getVideos() {
        return videos == null ? videos = new HashMap<>() : videos;
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

    public void update(String name, long time) {
        this.name = name;
        this.time = time;
    }
}
