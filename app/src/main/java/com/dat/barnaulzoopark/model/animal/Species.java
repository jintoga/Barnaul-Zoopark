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
public class Species extends AbstractData {
    private String uid;
    private String categoryUid;
    private String name;
    private String icon;
    private String thumbnail;
    private String description;
    private Map<String, String> animals = new HashMap<>();

    public Species() {
        // Default constructor required for calls to DataSnapshot.getValue(Species.class)
    }

    public String getUid() {
        return uid;
    }

    public String getCategoryUid() {
        return categoryUid;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Map<String, String> getAnimals() {
        return animals;
    }

    @Exclude
    @Override
    public String getPhotoUrl() {
        return getIcon();
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
