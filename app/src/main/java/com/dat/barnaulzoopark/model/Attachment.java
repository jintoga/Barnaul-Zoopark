package com.dat.barnaulzoopark.model;

/**
 * Created by Nguyen on 9/22/2016.
 */
public class Attachment {
    private boolean isFilled;
    private String filePath;

    public Attachment() {
    }

    public Attachment(boolean isFilled, String filePath) {
        this.isFilled = isFilled;
        this.filePath = filePath;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
