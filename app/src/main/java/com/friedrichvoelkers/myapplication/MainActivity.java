package com.friedrichvoelkers.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.friedrichvoelkers.myapplication.ui.SelectOptions;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button goToMapActivityButton;
    private Button goToMap2ActivityButton;
    private Button openOptions;

    String msg = "Android : ";

    // Default lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        goToMap2ActivityButton = findViewById(R.id.button_go_to_map2_activity);
        goToMap2ActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap2Activity();
            }
        });

        openOptions = findViewById(R.id.open_options);
        openOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionActivity();
            }
        });

        Log.d(msg, "The onCreate() event");
    }

    // Methods for navigation
    public void openOptionActivity() {
        Intent intent = new Intent(this, SelectOptions.class);
        startActivity(intent);
    }

    public void openMapActivity() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void openMap2Activity() {
        Intent intent = new Intent(this, Map2.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
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