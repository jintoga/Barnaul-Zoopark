package com.dat.barnaulzoopark.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by DAT on 1/8/2017.
 */
@IgnoreExtraProperties
public class User {
    private String name;
    private String email;
    private String photo;
    private boolean admin;

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
}
