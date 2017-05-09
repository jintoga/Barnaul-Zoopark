package com.dat.barnaulzoopark.model.animal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.AbstractData;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 3/10/2017.
 */
@IgnoreExtraProperties
public class Animal extends AbstractData {
    private String uid;
    private String name;
    private String speciesUid;
    private String photoSmall;
    private String photoBig;
    private String aboutOurAnimal;
    private boolean gender; //Male - true / Female - false
    private Long timeEnter;
    private Long dateOfBirth;
    private Map<String, String> photos;
    private String video;
    private String imageHabitatMap;

    public Animal() {
        // Default constructor required for calls to DataSnapshot.getValue(Animal.class)
    }

    public Animal(String uid, String name, String speciesUid, String aboutOurAnimal, boolean gender,
        long timeEnter) {
        this.uid = uid;
        this.name = name;
        this.speciesUid = speciesUid;
        this.aboutOurAnimal = aboutOurAnimal;
        this.gender = gender;
        this.timeEnter = timeEnter;
    }

    public String getUid() {
        return uid;
    }

    public String getSpeciesUid() {
        return speciesUid;
    }

    public String getName() {
        return name;
    }

    public String getPhotoSmall() {
        return photoSmall;
    }

    public String getPhotoBig() {
        return photoBig;
    }

    public String getAboutOurAnimal() {
        return aboutOurAnimal;
    }

    public boolean isGender() {
        return gender;
    }

    public long getTimeEnter() {
        return timeEnter;
    }

    @Nullable
    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    @NonNull
    public Map<String, String> getPhotos() {
        return photos == null ? photos = new HashMap<>() : photos;
    }

    public void setSpeciesUid(String speciesUid) {
        this.speciesUid = speciesUid;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getImageHabitatMap() {
        return imageHabitatMap;
    }

    @Exclude
    @Override
    public String getPhotoUrl() {
        return getPhotoSmall();
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
