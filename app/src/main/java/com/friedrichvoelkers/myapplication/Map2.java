package com.friedrichvoelkers.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

public class Map2 extends AppCompatActivity {

    private MapView mapView;

    private GeoJsonOptions geoJsonOptions;
    private SymbolManager symbolManager;
    private Symbol symbol;

    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map2);

        mapView = (MapView) findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(52.516, 13.3777)));

                        style.addImage("location_marker",
                                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.location_marker)),
                                true);

                        geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);
                        symbolManager = new SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions);

                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setTextAllowOverlap(true);

                        SymbolOptions symbolOptions = new SymbolOptions()
                                .withLatLng(new LatLng( 52.516, 13.3777))
                                .withIconImage("location_marker")
                                .withIconSize(1.3f)
                                .withSymbolSortKey(10.0f)
                                .withDraggable(true);
                        symbol = symbolManager.create(symbolOptions);
                    }
                });
            }
        });

        // UPDATE BUTTON
        updateButton = findViewById(R.id.map_2_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButton();

            }
        });
    }

    private void updateButton() {
        //symbol.setLatLng(new LatLng(52.517, 13.3787));
        symbol.setGeometry(Point.fromLngLat(13.3787, 52.517));
        symbolManager.update(symbol);
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