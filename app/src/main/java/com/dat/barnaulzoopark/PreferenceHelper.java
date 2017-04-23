package com.dat.barnaulzoopark;

import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by DAT on 4/23/2017.
 */
@Singleton
public class PreferenceHelper {

    public static final String PREF_FILE_NAME = "BZSharedPreference";

    public static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";
    public static final String KEY_IS_ADMIN = "KEY_IS_ADMIN";

    private final SharedPreferences preferences;

    @Inject
    public PreferenceHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setIsAdmin(boolean isAdmin) {
        preferences.edit().putBoolean(KEY_IS_ADMIN, isAdmin).apply();
    }

    public boolean isAdmin() {
        return preferences.getBoolean(KEY_IS_ADMIN, false);
    }
}