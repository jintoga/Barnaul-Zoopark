package com.dat.barnaulzoopark.model.animal;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 3/10/2017.
 */
@IgnoreExtraProperties
public class Animal {
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

    public Animal() {
        // Default constructor required for calls to DataSnapshot.getValue(Animal.class)
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
}
