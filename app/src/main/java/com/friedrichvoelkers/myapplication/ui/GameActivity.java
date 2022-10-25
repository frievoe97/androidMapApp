package com.friedrichvoelkers.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.friedrichvoelkers.myapplication.R;
import com.friedrichvoelkers.myapplication.gameEngine.GameEngine;
import com.friedrichvoelkers.myapplication.users.User;
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
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private MapView gameMapView;
    private SymbolManager symbolManager;

    private CountDownTimer countdownTimer;

    private TextView countdownText;

    private Button startButton;
    private Button stopButton;

    private ProgressBar progressBar;

    private static final String loggerTag = "GameActivity";

    private static final String LOCATION_MARKER_BLUE = "map_marker_blue";
    private static final String LOCATION_MARKER_RED = "map_marker_red";

    private static final double SOUTH_LIMIT = 52.510744;
    private static final double NORTH_LIMIT = 52.522980;
    private static final double WEST_LIMIT = 13.385971;
    private static final double EAST_LIMIT = 13.370694;

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MILISECONDS_PER_SECOND = 1000;
    private static final int SHOW_MR_X_FREQUENCY_IN_MINUTES = 5;
    private static final int GAME_DURATION_IN_MINUTES = 30;

    private int gameTimeLeft;               // time in seconds
    private int mrXNewLocationTimeLeft;     // time in seconds

    private GameEngine gameEngine;
    private HashMap<Integer, User> allUser = new HashMap<>();
    private HashMap<Integer, Symbol> allUserSymbols = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mrXNewLocationTimeLeft = SHOW_MR_X_FREQUENCY_IN_MINUTES * SECONDS_PER_MINUTE;

        // Add dummy user data
        createDummyUserHashmap();

        // Add dummy gameEngine
        createDummyGameEngine();

        // All The Map Stuff...
        // Set up the start camera position (must be deleted later and show current position)
        CameraPosition startCameraPosition = new CameraPosition.Builder()
                .target(new LatLng(52.516275, 13.377704))
                .zoom(14)
                .build();

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_game);

        gameMapView = (MapView) findViewById(R.id.gameMapView);
        gameMapView.onCreate(savedInstanceState);
        gameMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                // DIsable mapbox logo and info button
                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setLogoEnabled(false);

                mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Add camera position to map
                        mapboxMap.setCameraPosition(startCameraPosition);

                        // Initialize SymbolManager
                        symbolManager = new SymbolManager(gameMapView, mapboxMap, style);

                        // Add location marker (map marker in blue and red)
                        addLocationMarkerToStyle(style);

                        // Create for each user a symbol and add the user to the allUserSymbol HashMap
                        createDummyUserSymbolHashmap();
                    }
                });
            }
        });

        countdownText = findViewById(R.id.text_show_countdown);
        countdownText.setText(convertTimeToString(mrXNewLocationTimeLeft));

        // Init Buttons
        startButton = findViewById(R.id.button_start_move_user);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countdownTimer.start();
            }
        });

        stopButton = findViewById(R.id.button_stop_move_user);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countdownTimer.cancel();
            }
        });

        // Init ProgressBar
        progressBar = findViewById(R.id.game_progress_bar);
        progressBar.setProgress(100);


        countdownTimer = new CountDownTimer(GAME_DURATION_IN_MINUTES * SECONDS_PER_MINUTE * MILISECONDS_PER_SECOND, 1000) {
            @Override
            public void onTick(long l) {
                mrXNewLocationTimeLeft--;

                // Change text
                countdownText.setText(convertTimeToString(mrXNewLocationTimeLeft));

                // Change ProgressBar
                progressBar.setProgress(updateProgressBar(mrXNewLocationTimeLeft));

                // Update Uer Locations
                updateDummyUserLocations();
            }

            @Override
            public void onFinish() {
                Log.d(loggerTag, "Countdown Finish");
            }
        };
    }

    private int updateProgressBar (int time) {
        return (int) ((100.0 / SHOW_MR_X_FREQUENCY_IN_MINUTES * SECONDS_PER_MINUTE) * time);
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

    private void createDummyGameEngine() {
        gameEngine = new GameEngine(createRandomIntBetweenTwoValues(1000, 9999),
                SHOW_MR_X_FREQUENCY_IN_MINUTES * SECONDS_PER_MINUTE, GAME_DURATION_IN_MINUTES * SECONDS_PER_MINUTE);
    }

    private void updateDummyUserLocations() {
        for (java.util.Map.Entry<Integer, Symbol> entry : allUserSymbols.entrySet()) {
            entry.getValue().setLatLng(createRandomCoordinates());
            symbolManager.update(entry.getValue());
        }
    }

    private void createDummyUserSymbolHashmap() {
        for (Map.Entry<Integer, User> entry : allUser.entrySet()) {
            if (entry.getValue().isMrX()) {
                allUserSymbols.put(entry.getKey(), symbolManager.create(new SymbolOptions().withLatLng(createRandomCoordinates()).withIconImage(LOCATION_MARKER_RED).withIconSize(1.3f).withIconColor("#FF5050")));
            } else {
                allUserSymbols.put(entry.getKey(), symbolManager.create(new SymbolOptions().withLatLng(createRandomCoordinates()).withIconImage(LOCATION_MARKER_BLUE).withIconSize(1.3f).withIconColor("#00AEEF")));
            }
        }
    }

    private void createDummyUserHashmap() {
        User user1 = new User("frie_voe", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user2 = new User("postMalone", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user3 = new User("HTWilfried", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user4 = new User("Yolo_1212", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user5 = new User("MasterOfJava", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user6 = new User("PythonGuy123", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user7 = new User("pirate34", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user8 = new User("swagger45", createRandomIntBetweenTwoValues(1000, 9999),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);

        allUser.put(user1.getId(), user1);
        allUser.put(user2.getId(), user2);
        allUser.put(user3.getId(), user3);
        allUser.put(user4.getId(), user4);
        allUser.put(user5.getId(), user5);
        allUser.put(user6.getId(), user6);
        allUser.put(user7.getId(), user7);
        allUser.put(user8.getId(), user8);
    }

    private void addLocationMarkerToStyle(Style style) {
        style.addImage(LOCATION_MARKER_BLUE,
                Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.map_marker_blue))),
                true);

        style.addImage(LOCATION_MARKER_RED,
                Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.map_marker_red))),
                true);
    }

    private int createRandomIntBetweenTwoValues(int intMin, int intMax) {
        return intMin + (int)(Math.random() * ((intMax - intMin) + 1));
    }

    private double createRandomDoubleBetweenTwoValues(double doubleMin, double doubleMax) {
        return doubleMin + new Random().nextDouble() * (doubleMax - doubleMin);
    }

    private LatLng createRandomCoordinates() {
        return new LatLng(createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT));
    }
}