package com.dat.barnaulzoopark.model.animal;

import com.dat.barnaulzoopark.model.AbstractData;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 3/28/2017.
 */
@IgnoreExtraProperties
public class Category extends AbstractData {
    private String uid;
    private String name;
    private String icon;
    private String description;
    private Map<String, String> species = new HashMap<>();

    public Category() {
        // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    }

    public Category(String uid, String name, String description) {
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    public String getUid() {
        return uid;
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

    public Map<String, String> getSpecies() {
        return species;
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
