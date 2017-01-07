package com.dat.barnaulzoopark.events;

/**
 * Created by DAT on 1/7/2017.
 */

public class LoggedIn {

    private boolean isAdmin;

    public LoggedIn(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
