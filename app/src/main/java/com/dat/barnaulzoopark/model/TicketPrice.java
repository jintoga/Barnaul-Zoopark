package com.dat.barnaulzoopark.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by DAT on 5/20/2017.
 */
@IgnoreExtraProperties
public class TicketPrice extends AbstractData {
    private String uid;
    private String name;
    private Double price;
    private String icon;

    public TicketPrice() {
    }

    public TicketPrice(String uid, String name, Double price) {
        this.uid = uid;
        this.name = name;
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getIcon() {
        return icon;
    }

    public void update(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public void clearIcon() {
        this.icon = null;
    }

    @Exclude
    @Override
    public String getPhotoUrl() {
        return getIcon();
    }

    @Exclude
    @Override
    public String getText() {
        return null;
    }

    @Exclude
    @Override
    public String getId() {
        return null;
    }
}
