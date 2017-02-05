package com.dat.barnaulzoopark.model;

import android.net.Uri;

/**
 * Created by Nguyen on 9/22/2016.
 */
public class Attachment {
    private boolean isFilled;
    private Uri uri;

    public Attachment() {
    }

    public Attachment(boolean isFilled, Uri uri) {
        this.isFilled = isFilled;
        this.uri = uri;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
