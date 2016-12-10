package com.dat.barnaulzoopark.model;

/**
 * Created by DAT on 12/9/2016.
 */

public class Animal {
    String name;
    //thumbnail image URL
    String imageUrl;
    //banner image URL
    String bannerImageUrl;
    String soundUrl;
    boolean isSoundPlaying = false;

    public Animal() {
    }

    public Animal(String name, String imageUrl, String soundUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.soundUrl = soundUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }
}
