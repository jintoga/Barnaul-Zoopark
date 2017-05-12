package com.dat.barnaulzoopark;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dat.barnaulzoopark.model.User;
import com.google.gson.Gson;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by DAT on 4/23/2017.
 */
@Singleton
public class PreferenceHelper {

    private static final String PREF_FILE_NAME = "BZSharedPreference";

    private static final String KEY_USER = "KEY_USER";
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";
    private static final String KEY_IS_ADMIN = "KEY_IS_ADMIN";

    private final SharedPreferences preferences;

    @Inject
    PreferenceHelper(Context context) {
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

    boolean isAdmin() {
        return preferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public void setUser(@NonNull User user) {
        String userJson = new Gson().toJson(user);
        preferences.edit().putString(KEY_USER, userJson).apply();
    }

    @Nullable
    User getUser() {
        String userJson = preferences.getString(KEY_USER, null);
        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        }
        return null;
    }
}
