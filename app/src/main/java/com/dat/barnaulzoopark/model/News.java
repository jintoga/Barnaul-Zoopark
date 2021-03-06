package com.dat.barnaulzoopark.model;

import android.support.annotation.Nullable;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 1/8/2017.
 */
@IgnoreExtraProperties
public class News extends AbstractData {
    private String uid;
    private String title;
    private String description;
    private String thumbnail;
    private long time;
    private Map<String, String> photos = new HashMap<>();
    private String video;

    public News() {
        // Default constructor required for calls to DataSnapshot.getValue(News.class)
    }

    public News(String uid, String title, String description, long time) {
        this.uid = uid;
        this.title = title;
        this.description = description;
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

    @Nullable
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Map<String, String> getPhotos() {
        return photos;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
        this.time = Calendar.getInstance().getTimeInMillis();
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
