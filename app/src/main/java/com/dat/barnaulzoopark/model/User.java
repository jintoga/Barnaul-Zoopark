package com.dat.barnaulzoopark.model;

import android.support.annotation.NonNull;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 1/8/2017.
 */
@IgnoreExtraProperties
public class User {
    private String name;
    private String email;
    private String photo;
    private boolean admin;
    private Map<String, String> subscribedAnimals = new HashMap<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String photo, boolean admin) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isAdmin() {
        return admin;
    }

    @NonNull
    public Map<String, String> getSubscribedAnimals() {
        return subscribedAnimals == null ? subscribedAnimals = new HashMap<>() : subscribedAnimals;
    }
}
