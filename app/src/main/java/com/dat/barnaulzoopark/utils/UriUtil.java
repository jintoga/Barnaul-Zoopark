package com.dat.barnaulzoopark.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;

/**
 * Created by DAT on 2/23/2017.
 */

public class UriUtil {
    public static boolean isLocalFile(@NonNull Uri uri) {
        return URLUtil.isFileUrl(uri.toString());
    }
}
