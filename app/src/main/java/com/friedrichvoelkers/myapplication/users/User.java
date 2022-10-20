package com.friedrichvoelkers.myapplication.users;

public class User {

    // TODO Tests: in each game has to be one gamemaster (not more and not less)
    // TODO Tests: in each game has to be one mrX (not more and not less)

    // Variables
    private String nickname;
    private int id;
    private double currentLongitude;
    private double currentLatitude;
    private double lastLongitude;       // could be deleted maybe
    private double lastLatitude;        // could be deleted maybe
    private boolean isMrX;
    private boolean isGameMaster;

    // Constructor
    public User(String nickname, int id, double currentLongitude, double currentLatitude, double lastLongitude, double lastLatitude, boolean isMrX, boolean isGameMaster) {
        this.nickname = nickname;
        this.id = id;
        this.currentLongitude = currentLongitude;
        this.currentLatitude = currentLatitude;
        this.lastLongitude = lastLongitude;
        this.lastLatitude = lastLatitude;
        this.isMrX = isMrX;
        this.isGameMaster = isGameMaster;
    }

    // Custom Methods
    public void updateLocation(double latitude, double longitude) {
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
    }

    // Getter and Setter
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public boolean isMrX() {
        return isMrX;
    }

    public void setMrX(boolean mrX) {
        isMrX = mrX;
    }

    public boolean isGameMaster() {
        return isGameMaster;
    }

    public void setGameMaster(boolean gameMaster) {
        isGameMaster = gameMaster;
    }
}
