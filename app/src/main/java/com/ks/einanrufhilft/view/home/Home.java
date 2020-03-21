package com.ks.einanrufhilft.view.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.persistance.OrderDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 100;

    private List<OrderDTO> orders;

    private GoogleMap map;
    private boolean hasLocationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        initalizeData();
    }

    private void initalizeData() {
        orders = new ArrayList<>();
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());

        hasLocationPermission = false;
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
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Center map on Germany
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51, 10), 5));
        map.setMyLocationEnabled(hasLocationPermission);
    }
}
