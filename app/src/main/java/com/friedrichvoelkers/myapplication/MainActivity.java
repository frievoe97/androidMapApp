package com.friedrichvoelkers.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.friedrichvoelkers.myapplication.chat.ChatActivity;
import com.friedrichvoelkers.myapplication.chat.Message;
import com.friedrichvoelkers.myapplication.data.GlobalState;
import com.friedrichvoelkers.myapplication.gameEngine.GameEngine;
import com.friedrichvoelkers.myapplication.users.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button buttonCreateNewGame;
    private Button buttonJoinGame;
    private Button buttonOpenChat;
    private Button goToMapActivityButton;

    private EditText gameIdTextInput;
    private EditText nicknameTextInput;

    private DatabaseReference firebaseRealtimeDatabase;
    private DatabaseReference firebaseRealtimeDatabaseGames;
    private DatabaseReference firebaseRealtimeDatabaseUser;
    private DatabaseReference firebaseRealtimeDatabaseGame;

    private int gameID;
    private String nickname;

    String msg = "Android : ";

    //GlobalState gs = (GlobalState) getApplication();

    private ArrayList<Integer> allExsistingGameIds = new ArrayList<>();

    // Default lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseRealtimeDatabase =  FirebaseDatabase.getInstance().getReference();
        firebaseRealtimeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot data : snapshot.getChildren()) {
                        Log.d("All Keys in root: ", data.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        firebaseRealtimeDatabaseGames = firebaseRealtimeDatabase.child("games");
        firebaseRealtimeDatabaseGames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    allExsistingGameIds.clear();
                    for(DataSnapshot data : snapshot.getChildren()) {
                        allExsistingGameIds.add(Integer.valueOf(data.getKey()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*

        User user1 = new User("frie_voe", 3551, 53.13, 13.3452, 53.13, 13.3452, false, true);
        User user2 = new User("lolmaster123", 5231, 53.13, 13.3452, 53.13, 13.3452, false, false);
        User user3 = new User("MrYXZ", 1645, 53.13, 13.3452, 53.13, 13.3452, true, false);

        GameEngine gameEngine = new GameEngine(gameID, 1000, 13000);

        // Firebase
        firebaseRealtimeDatabase =  FirebaseDatabase.getInstance().getReference().child("games").child(String.valueOf(gameID));

        firebaseRealtimeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(msg, snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseRealtimeDatabaseGame = firebaseRealtimeDatabase.child("game");
        firebaseRealtimeDatabaseUser = firebaseRealtimeDatabase.child("user");

        firebaseRealtimeDatabaseUser.child(String.valueOf(user1.getId())).setValue(user1);
        firebaseRealtimeDatabaseUser.child(String.valueOf(user2.getId())).setValue(user2);
        firebaseRealtimeDatabaseUser.child(String.valueOf(user3.getId())).setValue(user3);

        firebaseRealtimeDatabaseGame.setValue(gameEngine);

        //Task snapshot = firebaseRealtimeDatabase.get();

        //Log.d(msg, snapshot.getResult().toString());

        */
        /*


        // init buttons
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

        goToMapActivityButton = findViewById(R.id.button_go_to_map_activity);
        goToMapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapActivity();
            }
        });

        logInButton = findViewById(R.id.login_menue);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        */

        goToMapActivityButton = findViewById(R.id.button_go_to_map_activity);
        goToMapActivityButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), Map.class);
            startActivity(intent);
        });

        nicknameTextInput = findViewById(R.id.nickname_text);
        gameIdTextInput = findViewById(R.id.game_id_text);

        buttonJoinGame = findViewById(R.id.main_button_connect_game);
        buttonJoinGame.setOnClickListener(view -> {

            GlobalState.setGameId(Integer.parseInt(String.valueOf(gameIdTextInput.getText())));
            GlobalState.setNickname(String.valueOf(nicknameTextInput.getText()));

            // Get All IDs
            firebaseRealtimeDatabaseGames.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for(DataSnapshot data : snapshot.getChildren()) {
                            allExsistingGameIds.add(Integer.parseInt(data.getKey()));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (allExsistingGameIds.contains(GlobalState.getGameId())) {
                GlobalState.addUser(new User(GlobalState.getNickname(), GlobalState.getNickname().hashCode(), 53.4,
                        13.5, 53.4, 13.5, false, false));
                firebaseRealtimeDatabase.child("games").child(String.valueOf(GlobalState.getGameId())).child("user").push()
                        .setValue(GlobalState.getAllUserGlobal().get(GlobalState.getNickname().hashCode()));
            } else Toast.makeText(getApplicationContext(), "Game ID dont't exists!", Toast.LENGTH_LONG).show();
        });


        buttonCreateNewGame = findViewById(R.id.main_button_create_new_game);
        buttonCreateNewGame.setOnClickListener(view -> {
            GlobalState.setGameId(Integer.parseInt(String.valueOf(gameIdTextInput.getText())));
            GlobalState.setNickname(String.valueOf(nicknameTextInput.getText()));

            GlobalState.addUser(new User(GlobalState.getNickname(), GlobalState.getNickname().hashCode(), 53.4,
                    13.5, 53.4, 13.5, false, true));

            GlobalState.createGameEngine(new GameEngine(GlobalState.getGameId(), 1, 5));

            //firebaseRealtimeDatabase.child("games").child(String.valueOf(GlobalState.getGameId())).child("user").child(String.valueOf(GlobalState.getNickname().hashCode())).setValue(GlobalState.getAllUserGlobal().get(GlobalState.getNickname().hashCode()));
            firebaseRealtimeDatabase.child("games").child(String.valueOf(GlobalState.getGameId())).child("user").push()
                    .setValue(GlobalState.getAllUserGlobal().get(GlobalState.getNickname().hashCode()));
            firebaseRealtimeDatabase.child("games").child(String.valueOf(GlobalState.getGameId())).child("game").setValue(GlobalState.getGameEngineGlobal());
            firebaseRealtimeDatabase.child("games").child(String.valueOf(GlobalState.getGameId())).child("messages").setValue(new ArrayList<>());
        });


        // GO TO MAP
        goToMapActivityButton = findViewById(R.id.button_go_to_map_activity);
        goToMapActivityButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), Map.class);
            startActivity(intent);
        });

        // GO TO CHAT
        buttonOpenChat = findViewById(R.id.main_button_open_chat);
        buttonOpenChat.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ChatActivity.class);
            startActivity(intent);
        });

        Log.d(msg, "The onCreate() event");
    }

    public void openMap2Activity() {
        Intent intent = new Intent(this, ChatAppActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }
}