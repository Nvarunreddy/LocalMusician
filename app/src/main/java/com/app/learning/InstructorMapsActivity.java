package com.app.learning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class InstructorMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnSelectLocation = findViewById(R.id.select_location_button);
        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marker != null) {
                    LatLng selectedLatLng = marker.getPosition();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_LATITUDE, selectedLatLng.latitude);
                    intent.putExtra(Constants.EXTRA_LONGITUDE, selectedLatLng.longitude);
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    setResult(Activity.RESULT_CANCELED);
                }
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng initialLatLng = new LatLng(-34, 151);
        marker = mMap.addMarker(new MarkerOptions().position(initialLatLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 10));

        mMap.setOnMapLongClickListener(this);

        // Enable the zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }
}
