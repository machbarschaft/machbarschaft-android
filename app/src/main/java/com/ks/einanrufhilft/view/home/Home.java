package com.ks.einanrufhilft.view.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.persistance.OrderDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = "Home";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 100;

    private List<OrderDTO> orders;

    private GoogleMap map;
    private boolean hasLocationPermission;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        initializeData();
    }

    private void initializeData() {
        orders = new ArrayList<>();
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());

        hasLocationPermission = false;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void requestCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(LOG_TAG, "Last known location: " + location);
                        if (location == null || map == null) {
                            return;
                        }

                        LatLng llPos = new LatLng(location.getLatitude(), location.getLongitude());
                        float zoom = 14f;
                        if (location.getAccuracy() > 10000) {
                            zoom = 5f;
                        } else if (location.getAccuracy() > 1000) {
                            zoom = 10f;
                        }

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(llPos, zoom));
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            hasLocationPermission = true;
        } else {
            // Need to ask user for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasLocationPermission = true;
                if (map != null) {
                    map.setMyLocationEnabled(true);
                }
                requestCurrentLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(hasLocationPermission);
        if (hasLocationPermission) {
            requestCurrentLocation();
        }
    }
}
