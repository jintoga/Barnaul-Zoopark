package com.dat.barnaulzoopark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DAT on 12/9/2016.
 */

public class Animal implements Parcelable {
    private String species;
    //thumbnail image URL
    private String imageUrl;
    //banner image URL
    private String bannerImageUrl;
    private String soundUrl;
    private String aboutOurAnimals;
    private String characteristics;
    private String aboutSpecies;
    private String facts;

    public Animal() {
    }

    public Animal(String species, String imageUrl, String bannerImageUrl, String soundUrl,
        String aboutOurAnimals, String characteristics, String aboutSpecies, String facts) {
        this.species = species;
        this.imageUrl = imageUrl;
        this.bannerImageUrl = bannerImageUrl;
        this.soundUrl = soundUrl;
        this.aboutOurAnimals = aboutOurAnimals;
        this.characteristics = characteristics;
        this.aboutSpecies = aboutSpecies;
        this.facts = facts;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
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

    protected Animal(Parcel in) {
        species = in.readString();
        imageUrl = in.readString();
        bannerImageUrl = in.readString();
        soundUrl = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(species);
        parcel.writeString(imageUrl);
        parcel.writeString(bannerImageUrl);
        parcel.writeString(soundUrl);
        parcel.writeString(aboutOurAnimals);
        parcel.writeString(characteristics);
        parcel.writeString(aboutSpecies);
        parcel.writeString(facts);
    }
}
