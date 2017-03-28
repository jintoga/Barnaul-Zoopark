package com.dat.barnaulzoopark.model.animal;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 3/10/2017.
 */
@IgnoreExtraProperties
public class Species {
    private String uid;
    private String categoryUid;
    private String name;
    private String icon;
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

    public Map<String, String> getAnimals() {
        return animals;
    }
}
