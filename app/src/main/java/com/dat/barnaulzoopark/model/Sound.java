package com.dat.barnaulzoopark.model;

/**
 * Created by DAT on 11/9/2016.
 */

public class Sound {
    private String url;
    private boolean isPlaying = false;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
