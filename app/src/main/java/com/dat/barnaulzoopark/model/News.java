package com.dat.barnaulzoopark.model;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 1/8/2017.
 */
@IgnoreExtraProperties
public class News {
    private String uid;
    private String title;
    private String description;
    private String thumbnail;
    private long time;
    private Map<String, String> photos = new HashMap<>();
    private Map<String, String> videos = new HashMap<>();

    public News() {
        // Default constructor required for calls to DataSnapshot.getValue(News.class)
    }

    public News(String uid, String title, String description, String thumbnail, long time) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getTime() {
        return time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Map<String, String> getPhotos() {
        return photos;
    }

    public Map<String, String> getVideos() {
        return videos;
    }
}
