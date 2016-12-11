package com.dat.barnaulzoopark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DAT on 12/9/2016.
 */

public class Animal implements Parcelable {
    String name;
    //thumbnail image URL
    String imageUrl;
    //banner image URL
    String bannerImageUrl;
    String soundUrl;
    boolean isSoundPlaying = false;

    String aboutOurAnimals;
    String characteristics;
    String aboutSpecies;
    String facts;

    public Animal() {
    }

    public Animal(String name, String imageUrl, String bannerImageUrl, String soundUrl,
        boolean isSoundPlaying, String aboutOurAnimals, String characteristics, String aboutSpecies,
        String facts) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.bannerImageUrl = bannerImageUrl;
        this.soundUrl = soundUrl;
        this.isSoundPlaying = isSoundPlaying;
        this.aboutOurAnimals = aboutOurAnimals;
        this.characteristics = characteristics;
        this.aboutSpecies = aboutSpecies;
        this.facts = facts;
    }

    protected Animal(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        bannerImageUrl = in.readString();
        soundUrl = in.readString();
        isSoundPlaying = in.readByte() != 0;
        aboutOurAnimals = in.readString();
        characteristics = in.readString();
        aboutSpecies = in.readString();
        facts = in.readString();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(bannerImageUrl);
        dest.writeString(soundUrl);
        dest.writeByte((byte) (isSoundPlaying ? 1 : 0));
        dest.writeString(aboutOurAnimals);
        dest.writeString(characteristics);
        dest.writeString(aboutSpecies);
        dest.writeString(facts);
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

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public boolean isSoundPlaying() {
        return isSoundPlaying;
    }

    public void setSoundPlaying(boolean soundPlaying) {
        isSoundPlaying = soundPlaying;
    }

    public String getAboutOurAnimals() {
        return aboutOurAnimals;
    }

    public void setAboutOurAnimals(String aboutOurAnimals) {
        this.aboutOurAnimals = aboutOurAnimals;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getAboutSpecies() {
        return aboutSpecies;
    }

    public void setAboutSpecies(String aboutSpecies) {
        this.aboutSpecies = aboutSpecies;
    }

    public String getFacts() {
        return facts;
    }

    public void setFacts(String facts) {
        this.facts = facts;
    }
}
