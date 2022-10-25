package com.friedrichvoelkers.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.friedrichvoelkers.myapplication.R;

public class LogInActivity extends AppCompatActivity {

    Button buttonCreateNewGame;
    Button buttonJoinGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        buttonCreateNewGame = findViewById(R.id.button_create_new_game);
        buttonCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateNewGameActivity.class);
                startActivity(intent);
            }
        });

        buttonJoinGame = findViewById(R.id.button_join_game);
        buttonJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), JoinGameActivity.class);
                startActivity(intent);
            }
        });
    }
}