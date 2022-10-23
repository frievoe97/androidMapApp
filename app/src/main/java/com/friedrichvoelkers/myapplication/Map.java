package com.friedrichvoelkers.myapplication;

import static com.mapbox.mapboxsdk.maps.Style.DARK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

public class Map extends AppCompatActivity {

    private static final String LOCATION_MARKER = "location_marker";

    private MapView mapView;

    private Button goBackToMain;
    private Button startButton;
    private Button resetButton;
    private Button increaseButton;
    private Button decreaseButton;

    private boolean runCountdown;
    private TextView countdownText;
    private ProgressBar progressBar;
    private int totalTime;
    private CountDownTimer timer;
    private CountDownTimer timertest;
    private static Point ORIGIN_POINT = Point.fromLngLat(106.7140180, -6.149120972);
    private SymbolManager symbolManager;
    private SymbolOptions symbolBrandenburgGate;
    private SymbolManager symbolManagerMaster;
    private Symbol symbol1;
    private Symbol symbol2;
    private GeoJsonSource geoJsonSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(52.516275, 13.377704))
                .zoom(14)
                .tilt(20)
                .build();

        // Set total time
        totalTime = 10 * 60;

        // MAP Step 1
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_map);

        // MAP Step 2
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        mapboxMap.setCameraPosition(position);



                        addLocationMarkerToStyle(style);
                        symbolManagerMaster = new SymbolManager(mapView, mapboxMap, style);


                    }
                });
            }
        });

        countdownText = (TextView)findViewById(R.id.text_time);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(updateProgressBar(convertStringToTime((String) countdownText.getText())));

        // GO BACK BUTTON
        goBackToMain = findViewById(R.id.button_go_to_main_activity);
        goBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        // GO BACK BUTTON
        startButton = findViewById(R.id.start_linear_progress);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMarker();
                //timer.start();
                //timertest.start();
            }
        });

        // GO BACK BUTTON
        resetButton = findViewById(R.id.reset_linear_progress);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMarker();
                //runCountdown = false;
                //countdownText.setText("10:00");
                //progressBar.setProgress(updateProgressBar(convertStringToTime((String) countdownText.getText())));
            }
        });

        // GO BACK BUTTON
        increaseButton = findViewById(R.id.increase_progress);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time = convertStringToTime((String) countdownText.getText());
                Log.d("time: ", String.valueOf(time));
                time++;
                Log.d("time: ", convertTimeToString(time));
                countdownText.setText(convertTimeToString(time));
                progressBar.setProgress(updateProgressBar(time));
            }
        });

        // GO BACK BUTTON
        decreaseButton = findViewById(R.id.decrease_progress);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time = convertStringToTime((String) countdownText.getText());
                Log.d("time: ", String.valueOf(time));
                time--;
                Log.d("time: ", convertTimeToString(time));
                countdownText.setText(convertTimeToString(time));
                progressBar.setProgress(updateProgressBar(time));
            }
        });

        timertest = new CountDownTimer(6 * 1000L, 500) {
            @Override
            public void onTick(long l) {
                Log.d("Timer 2: ", String.valueOf(l));

            }

            @Override
            public void onFinish() {
                Log.d("Restart: ", this.toString());
                this.start();

            }
        };

        timer = new CountDownTimer(totalTime * 1000L, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Timer 1: ", String.valueOf(millisUntilFinished));
                int time = convertStringToTime((String) countdownText.getText());
                time--;
                countdownText.setText(convertTimeToString(time));
                progressBar.setProgress(updateProgressBar(time));
            }

            @Override
            public void onFinish() {
                try{
                    Log.d("Timer: ", "FINISH");
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        };
    }

    private void updateMarker() {
        //symbol1.setLatLng(new LatLng(13.377704, 52.512275));
        //symbol2.setLatLng(new LatLng(13.374642, 52.512380));

        //symbolManagerMaster.update(symbol1);
        //symbolManagerMaster.update(symbol2);


        if (symbolManagerMaster.getAnnotations().containsValue(symbol1)) {
            Log.d("Contains: ", "true");
        } else Log.d("Contains: ", "false");

        Log.d("Annotations:", String.valueOf(symbolManagerMaster.getAnnotations()));
    }

    private void addMarker() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                Log.d("Hi", " my name is...");

                symbolBrandenburgGate = new SymbolOptions().withLatLng(new LatLng(52.516275,13.377704))
                        .withIconImage(LOCATION_MARKER)
                        .withIconSize(1.3f);
                symbol1 =  symbolManagerMaster.create(symbolBrandenburgGate);

                SymbolOptions symbolBrandenburgGate2 = new SymbolOptions().withLatLng(new LatLng(52.516380,13.374642))
                        .withIconImage(LOCATION_MARKER)
                        .withIconSize(1.3f);
                symbol2 =  symbolManagerMaster.create(symbolBrandenburgGate2);
            }
        });
    }

    private void addLocationMarkerToStyle (Style style) {
        style.addImage(LOCATION_MARKER,
                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.location_marker)),
                true);
    }

    private int updateProgressBar (int time) {
        int result = (int) ((100.0 / totalTime) * time);
        return result;
    }

    private int convertStringToTime(String time) {
        String[] timeArray = time.split(":");
        return Integer.parseInt(timeArray[0]) * 60 + Integer.parseInt(timeArray[1]);
    }

    private String convertTimeToString(int time) {
        int minutes = time % 60;
        minutes = (time - minutes) / 60;
        int seconds = (time - minutes * 60);
        if (seconds < 10) return minutes + ":0" + seconds;
        return minutes + ":" + seconds;
    }

    private void gameLoop() throws InterruptedException {
        while (runCountdown) {
            int time = convertStringToTime((String) countdownText.getText());
            time--;
            countdownText.setText(convertTimeToString(time));
            progressBar.setProgress(updateProgressBar(time));
            Thread.sleep(1000);
        }
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