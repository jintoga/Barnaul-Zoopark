package com.dat.barnaulzoopark.model;

/**
 * Created by DAT on 11/9/2016.
 */

public class AnimalData {
    private Photo photo;
    private Sound sound;

    public AnimalData(Photo photo, Sound sound) {
        this.photo = photo;
        this.sound = sound;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }
}
