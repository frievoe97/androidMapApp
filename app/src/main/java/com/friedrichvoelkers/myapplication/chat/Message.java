package com.friedrichvoelkers.myapplication.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {

    private static Date date = new Date();
    private static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm", Locale.GERMAN);

    private String name;
    private String message;
    private long time;

    // Is needed for the firebase
    public Message() {
    }

    public String getTimeString() {
        return timeString;
    }

    /*
    public void setTimeString(String timeString) {
        this.timeString = timeString;
    } */

    private String timeString = "";

    //private String icon;

    public Message(String name, String message, long time) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.timeString = timestampToTime(time);
        //this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    /*
    public void setMessage(String message) {
        this.message = message;
    } */

    public long getTime() {
        return time;
    }


    /* public void setTime(long time) {
        this.time = time;
    } */

    /*public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }*/

    public static String timestampToTime(long timestamp) {
        date.setTime(timestamp * 1000);
        return TIMEFORMAT.format(date);
    }

    public void addStringTime() {
        this.timeString = timestampToTime(this.getTime());
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", timeString='" + timeString + '\'' +
                '}';
    }
}
