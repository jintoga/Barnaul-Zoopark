package com.dat.barnaulzoopark.events;

/**
 * Created by DAT on 1/7/2017.
 */

public class LoggedInEvent {

    private boolean isAdmin;

    public LoggedInEvent(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
