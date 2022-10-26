package com.friedrichvoelkers.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
    private static final String LOCATION_MARKER = "map_marker_black";

    private static final double SOUTH_LIMIT = 52.510744;
    private static final double NORTH_LIMIT = 52.522980;
    private static final double WEST_LIMIT = 13.385971;
    private static final double EAST_LIMIT = 13.370694;
    private static final double CAMERA_POSITION_LATITUDE = 52.516275;
    private static final double CAMERA_POSITION_LONGITUDE = 13.3783325;

    private static final String STRING_TIME_SPLITTER = ":";

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MILISECONDS_PER_SECOND = 1000;
    private static final int SHOW_MR_X_FREQUENCY_IN_MINUTES = 5;
    private static final int GAME_DURATION_IN_MINUTES = 30;

    private static final int RANDOM_ID_MINIMUM = 1000;
    private static final int RANDOM_ID_MAXIMUM = 9999;

    private int gameTimeLeft;               // time in seconds
    private int mrXNewLocationTimeLeft;     // time in seconds

    private GameEngine gameEngine;
    private final HashMap<Integer, User> allUser = new HashMap<>();
    private final HashMap<Integer, Symbol> allUserSymbols = new HashMap<>();

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
        // .zoom(14)
        CameraPosition startCameraPosition = new CameraPosition.Builder()
                .target(new LatLng(CAMERA_POSITION_LATITUDE, CAMERA_POSITION_LONGITUDE))
                .zoom(14)
                .build();

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_game);

        gameMapView = findViewById(R.id.gameMapView);
        gameMapView.onCreate(savedInstanceState);
        gameMapView.getMapAsync(mapboxMap -> {
            // Disable mapbox logo and info button
            mapboxMap.getUiSettings().setAttributionEnabled(false);
            mapboxMap.getUiSettings().setLogoEnabled(false);

            mapboxMap.setStyle(Style.LIGHT, style -> {

                // Add camera position to map
                mapboxMap.setCameraPosition(startCameraPosition);

                // Initialize SymbolManager
                symbolManager = new SymbolManager(gameMapView, mapboxMap, style);

                // Add location marker (map marker in blue and red)
                addLocationMarkerToStyle(style);
            });
        });

        countdownText = findViewById(R.id.text_show_countdown);
        countdownText.setText(convertTimeToString(mrXNewLocationTimeLeft));

        // Init Buttons
        startButton = findViewById(R.id.button_start_move_user);
        startButton.setOnClickListener(view -> {
            // Create for each user a symbol and add the user to the allUserSymbol HashMap
            createDummyUserSymbolHashmap();
            countdownTimer.start();
        });

        stopButton = findViewById(R.id.button_stop_move_user);
        stopButton.setOnClickListener(view -> countdownTimer.cancel());

        // Init ProgressBar
        progressBar = findViewById(R.id.game_progress_bar);
        progressBar.setProgress(100);

        countdownTimer = new CountDownTimer(GAME_DURATION_IN_MINUTES * SECONDS_PER_MINUTE * MILISECONDS_PER_SECOND, 1000) {
            @Override
            public void onTick(long l) {
                mrXNewLocationTimeLeft = convertStringToTime((String) countdownText.getText());

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
        String[] timeArray = time.split(STRING_TIME_SPLITTER);
        return Integer.parseInt(timeArray[0]) * SECONDS_PER_MINUTE + Integer.parseInt(timeArray[1]);
    }

    private String convertTimeToString(int time) {
        int minutes = time % SECONDS_PER_MINUTE;
        minutes = (time - minutes) / SECONDS_PER_MINUTE;
        int seconds = (time - minutes * SECONDS_PER_MINUTE);
        if (seconds < 10) return minutes + ":0" + seconds;
        return minutes + ":" + seconds;
    }

    private void createDummyGameEngine() {
        gameEngine = new GameEngine(createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                SHOW_MR_X_FREQUENCY_IN_MINUTES * SECONDS_PER_MINUTE, GAME_DURATION_IN_MINUTES * SECONDS_PER_MINUTE);
    }

    private void updateDummyUserLocations() {
        boolean showAlready = false;
        for (java.util.Map.Entry<Integer, Symbol> integerSymbolEntry : allUserSymbols.entrySet()) {
            integerSymbolEntry.getValue().setLatLng(createRandomCoordinates());
            symbolManager.update(integerSymbolEntry.getValue());

            if (!showAlready && Objects.equals(integerSymbolEntry.getValue().getIconImage(), "map_marker_blue")) {
                showAlready = true;
                Log.d(loggerTag + " BLUE", String.valueOf(integerSymbolEntry.getValue()));
            }

            if (Objects.equals(integerSymbolEntry.getValue().getIconImage(), "map_marker_red")) {
                Log.d(loggerTag + " RED", String.valueOf(integerSymbolEntry.getValue()));
            }
        }
    }

    private void createDummyUserSymbolHashmap() {
        gameMapView.getMapAsync(mapboxMap -> {
            for (Map.Entry<Integer, User> integerUserEntry : allUser.entrySet()) {
                if (integerUserEntry.getValue().isMrX()) {
                    allUserSymbols.put(integerUserEntry.getKey(), symbolManager.create(new SymbolOptions().withLatLng(createRandomCoordinates()).withIconImage(LOCATION_MARKER).withIconSize(1.3f).withIconColor("#FF5050")));
                } else {
                    allUserSymbols.put(integerUserEntry.getKey(), symbolManager.create(new SymbolOptions().withLatLng(createRandomCoordinates()).withIconImage(LOCATION_MARKER).withIconSize(1.3f).withIconColor("#00AEEF")));
                }
            }
        });
    }

    private void createDummyUserHashmap() {
        User user1 = new User("frie_voe", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user2 = new User("postMalone", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, true);
        User user3 = new User("HTWilfried", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user4 = new User("Yolo_1212", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                true, false);
        User user5 = new User("MasterOfJava", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user6 = new User("PythonGuy123", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user7 = new User("pirate34", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT), createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT),
                false, false);
        User user8 = new User("swagger45", createRandomIntBetweenTwoValues(RANDOM_ID_MINIMUM, RANDOM_ID_MAXIMUM),
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

    // The official documentation use the same way
    // https://github.com/mapbox/mapbox-plugins-android/blob/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/annotation/SymbolActivity.java
    @SuppressLint("UseCompatLoadingForDrawables")
    private void addLocationMarkerToStyle(Style style) {

        style.addImage(LOCATION_MARKER,
                Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.map_marker))),
                true);

    }

    private int createRandomIntBetweenTwoValues(int intMin, int intMax) {
        return intMin + (int)(Math.random() * ((intMax - intMin) + 1));
    }

    private double createRandomDoubleBetweenTwoValues(double doubleMin, double doubleMax) {
        return doubleMin + new Random().nextDouble() * (doubleMax - doubleMin);
    }

    private LatLng createRandomCoordinates() {
        return new LatLng(createRandomDoubleBetweenTwoValues(SOUTH_LIMIT, NORTH_LIMIT),
                createRandomDoubleBetweenTwoValues(WEST_LIMIT, EAST_LIMIT));
    }

    // Lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        gameMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameMapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        gameMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        gameMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameMapView.onDestroy();
    }
}