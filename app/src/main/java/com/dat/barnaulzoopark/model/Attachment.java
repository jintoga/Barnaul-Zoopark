package com.dat.barnaulzoopark.model;

/**
 * Created by Nguyen on 9/22/2016.
 */
public class Attachment {
    private String attachmentUid;
    private boolean isFilled;
    private String url;

    public Attachment() {
    }

    public Attachment(boolean isFilled, String url) {
        this.isFilled = isFilled;
        this.url = url;
    }

    public String getAttachmentUid() {
        return attachmentUid;
    }

    public void setAttachmentUid(String attachmentUid) {
        this.attachmentUid = attachmentUid;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
