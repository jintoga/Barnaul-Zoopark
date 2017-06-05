package com.dat.barnaulzoopark.model;

import com.google.firebase.database.Exclude;

/**
 * Created by DAT on 6/4/2017.
 */

public class Sponsor extends AbstractData {
    private String uid;
    private String name;
    private String logo;
    private String site;

    public Sponsor() {
    }

    public Sponsor(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public void clearLogo() {
        this.logo = null;
    }

    public String getLogo() {
        return logo;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public void update(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Exclude
    @Override
    public String getPhotoUrl() {
        return getLogo();
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
