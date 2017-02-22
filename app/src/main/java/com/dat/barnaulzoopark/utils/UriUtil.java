package com.dat.barnaulzoopark.utils;

import android.support.annotation.NonNull;
import java.io.File;

/**
 * Created by DAT on 2/23/2017.
 */

public class UriUtil {
    public static boolean isLocalFile(@NonNull String url) {
        File file = new File(url);
        return file.exists();
    }
}
