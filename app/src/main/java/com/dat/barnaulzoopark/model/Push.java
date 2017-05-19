package com.dat.barnaulzoopark.model;

/**
 * Created by DAT on 5/15/2017.
 */

public class Push {
    private String to;
    private Data data;
    private Notification notification;

    public Push(String to, Data data, Notification notification) {
        this.to = to;
        this.data = data;
        this.notification = notification;
    }

    public static class Data {
        private String animalUid;
        private String blogUid;

        public Data(String animalUid, String blogUid) {
            this.animalUid = animalUid;
            this.blogUid = blogUid;
        }
    }

    public static class Notification {
        private String title;
        private String text;

        public Notification(String title, String text) {
            this.title = title;
            this.text = text;
        }
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
