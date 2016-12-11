package com.dat.barnaulzoopark.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DAT on 12/9/2016.
 */

public class Animal implements Parcelable{
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

    protected Animal(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        bannerImageUrl = in.readString();
        soundUrl = in.readString();
        isSoundPlaying = in.readByte() != 0;
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
