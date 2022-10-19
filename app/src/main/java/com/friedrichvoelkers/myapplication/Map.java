package com.friedrichvoelkers.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class Map extends AppCompatActivity {

    private MapView mapView;

    private Button goBackToMain;
    // private LinearProgressIndicator linearProgressIndicator;
    // private Button startLinearProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MAP Step 1
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_map);

        // MAP Step 2
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                    }
                });

            }
        });

        // GO BACK BUTTON
        goBackToMain = findViewById(R.id.button_go_to_main_activity);
        goBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        // LinearProgressIndicator
        // linearProgressIndicator = findViewById(R.id.linear_progress_indicator);
        // linearProgressIndicator.setIndicatorDirection(LinearProgressIndicator.INDICATOR_DIRECTION_LEFT_TO_RIGHT);
        // linearProgressIndicator.setIndeterminate(true);
        // linearProgressIndicator.setIndeterminateAnimationType(LinearProgressIndicator.INDETERMINATE_ANIMATION_TYPE_CONTIGUOUS);

/*        startLinearProgressButton = findViewById(R.id.start_linear_progress);
        startLinearProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Indicator: ","" + linearProgressIndicator.getVisibility());
                linearProgressIndicator.setVisibility(View.VISIBLE);
                Log.i("Indicator: ","" + linearProgressIndicator.getVisibility());
            }
        });*/
    }

    // Change Activity Methods
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}