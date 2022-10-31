package com.friedrichvoelkers.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.friedrichvoelkers.myapplication.chat.ChatActivity;
import com.friedrichvoelkers.myapplication.gameEngine.GameEngine;
import com.friedrichvoelkers.myapplication.ui.GameActivity;
import com.friedrichvoelkers.myapplication.ui.LogInActivity;
import com.friedrichvoelkers.myapplication.users.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button goToMapActivityButton;
    private Button goToMap2ActivityButton;
    private Button openOptions;
    private Button logInButton;

    private DatabaseReference firebaseRealtimeDatabase;
    private DatabaseReference firebaseRealtimeDatabaseUser;
    private DatabaseReference firebaseRealtimeDatabaseGame;

    private int gameID = 5324956;

    String msg = "Android : ";

    // Default lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        goToMap2ActivityButton = findViewById(R.id.button_go_to_chat_view);
        goToMap2ActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap2Activity();
            }
        });

        openOptions = findViewById(R.id.open_options);
        openOptions.setText("Open Chat");
        openOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                startActivity(intent);
                //Task snapshot = firebaseRealtimeDatabase.get();

                //Log.d(msg, snapshot.getResult().toString());
            }
        });

        Log.d(msg, "The onCreate() event");
    }

    private void openLogInMenu() {

    }

    // Methods for navigation
    public void openOptionActivity() {


    }

    public void openMapActivity() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void openMap2Activity() {
        Intent intent = new Intent(this, ChatAppActivity.class);
        startActivity(intent);
    }

    public void openActivity2() {

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