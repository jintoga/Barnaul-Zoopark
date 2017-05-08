package com.dat.barnaulzoopark.model.animal;

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
    private boolean gender;
    private long timeEnter;
    private long dateOfBirth;
    private Map<String, String> photos = new HashMap<>();
    private String video;

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

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public Map<String, String> getPhotos() {
        return photos;
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
