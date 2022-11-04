package com.friedrichvoelkers.myapplication.data;

import android.app.Application;

import com.friedrichvoelkers.myapplication.chat.Message;
import com.friedrichvoelkers.myapplication.gameEngine.GameEngine;
import com.friedrichvoelkers.myapplication.users.User;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalState extends Application {

    // game data
    private static GameEngine gameEngineGlobal = new GameEngine();

    // user data
    private static HashMap<Integer, User> allUserGlobal = new HashMap<>();

    // all messages
    private static ArrayList<Message> allMessagesGlobal = new ArrayList<>();

    // Just Values...
    private static int gameId;
    private static String nickname;


    // GameEngine
    public static void createGameEngine(GameEngine gameEngine) {
        gameEngineGlobal = gameEngine;
    }

    public static GameEngine getGameEngineGlobal () {
        return gameEngineGlobal;
    }

    // User
    // add user
    public static void createAllUserGlobal (HashMap<Integer, User> allUserGlobal) {
        allUserGlobal = allUserGlobal;
    }

    public static void addUser (User user) {
        allUserGlobal.put(user.getId(), user);
    }

    // get user data
    public static HashMap<Integer, User> getAllUserGlobal () {
        return allUserGlobal;
    }

    public static User getUserGlobal (Integer userId) {
        return allUserGlobal.get(userId);
    }

    public static void createAllMessagesGlobal (ArrayList<Message> allMessagesGlobalPara) {
        allMessagesGlobal = allMessagesGlobalPara;
    }

    public static void addMessageGlobal (Message message) {
        allMessagesGlobal.add(message);
    }

    public static ArrayList<Message> getAllMessagesGlobal () {
        return allMessagesGlobal;
    }


    public static int getGameId() {
        return gameId;
    }

    public static void setGameId(int gameIdPara) {
        gameId = gameIdPara;
    }

    public static String getNickname() {
        return nickname;
    }

    public static void setNickname(String nicknamePara) {
        nickname = nicknamePara;
    }
}
