package com.dat.barnaulzoopark.model;

/**
 * Created by DAT on 07-Feb-16.
 */
public class TempPhotoAlbum {

    private String id;
    private String name;
    private String date;
    private String[] urls;

    public TempPhotoAlbum() {
    }

    public TempPhotoAlbum(String name, String date, String[] urls) {
        this.name = name;
        this.date = date;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

}
