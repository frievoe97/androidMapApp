package com.friedrichvoelkers.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.friedrichvoelkers.myapplication.R;

public class CreateNewGameActivity extends AppCompatActivity {

    private Button buttonGameCreateDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_game);

        buttonGameCreateDone = findViewById(R.id.button_game_create_done);
        buttonGameCreateDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}