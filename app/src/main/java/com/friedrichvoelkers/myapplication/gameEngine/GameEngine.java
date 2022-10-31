package com.friedrichvoelkers.myapplication.gameEngine;

public class GameEngine {

    private int gameID;
    private int showMrXFrequency;   // Time in seconds
    private int gameDuration;       // Time in seconds
    private double centerLatitude = 52.516275;
    private double centerLongitude = 13.3783325;

    public GameEngine() {
    }

    public void startGameEngine() {}

    public GameEngine(int gameID, int showMrXFrequency, int gameDuration) {
        this.gameID = gameID;
        this.showMrXFrequency = showMrXFrequency;
        this.gameDuration = gameDuration;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getShowMrXFrequency() {
        return showMrXFrequency;
    }

    public void setShowMrXFrequency(int showMrXFrequency) {
        this.showMrXFrequency = showMrXFrequency;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    @Override
    public String toString() {
        return "GameEngine{" +
                "gameID=" + gameID +
                ", showMrXFrequency=" + showMrXFrequency +
                ", gameDuration=" + gameDuration +
                ", centerLatitude=" + centerLatitude +
                ", centerLongitude=" + centerLongitude +
                '}';
    }
}
