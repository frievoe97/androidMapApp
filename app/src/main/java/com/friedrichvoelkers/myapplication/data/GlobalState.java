package com.friedrichvoelkers.myapplication.data;

import android.app.Application;

import com.friedrichvoelkers.myapplication.chat.Message;
import com.friedrichvoelkers.myapplication.gameEngine.GameEngine;
import com.friedrichvoelkers.myapplication.users.User;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalState extends Application {

    // game data
    private GameEngine gameEngineGlobal = new GameEngine();

    // user data
    private HashMap<Integer, User> allUserGlobal = new HashMap<>();

    // all messages
    private ArrayList<Message> allMessagesGlobal = new ArrayList<>();


    // GameEngine
    public void createGameEngine(GameEngine gameEngine) {
        this.gameEngineGlobal = gameEngine;
    }

    public GameEngine getGameEngineGlobal () {
        return this.gameEngineGlobal;
    }

    // User
    // add user
    public void createAllUserGlobal (HashMap<Integer, User> allUserGlobal) {
        this.allUserGlobal = allUserGlobal;
    }

    public void addUser (User user) {
        allUserGlobal.put(user.getId(), user);
    }

    // get user data
    public HashMap<Integer, User> getAllUserGlobal () {
        return allUserGlobal;
    }

    public User getUserGlobal (Integer userId) {
        return allUserGlobal.get(userId);
    }

    public void createAllMessagesGlobal (ArrayList<Message> allMessagesGlobal) {
        this.allMessagesGlobal = allMessagesGlobal;
    }

    public void addMessageGlobal (Message message) {
        allMessagesGlobal.add(message);
    }

    public  ArrayList<Message> getAllMessagesGlobal () {
        return allMessagesGlobal;
    }





}
