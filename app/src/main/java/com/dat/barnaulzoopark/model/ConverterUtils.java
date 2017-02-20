package com.dat.barnaulzoopark.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DAT on 1/8/2017.
 */

public class ConverterUtils {
    public static String epochToString(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
