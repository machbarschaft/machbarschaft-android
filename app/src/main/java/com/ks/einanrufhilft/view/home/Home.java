package com.ks.einanrufhilft.view.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.OrderHandler;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.services.OrderInProgressNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Home extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = "Home";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 100;

    private List<Order> orderList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    private GoogleMap map;
    private boolean hasLocationPermission;
    private FusedLocationProviderClient fusedLocationClient;
    private Map<String, Marker> markerMap;
    private BitmapDescriptor markerIconNormal;
    private BitmapDescriptor markerIconUrgent;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeData();
        initView();
        startOrder();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopOrder();
    }

    /**
     * Initialize for Demo purposes some Data to display
     */
    private void initializeData() {
        this.orderList = OrderHandler.getInstance().getPersonInDistance(42000);
        System.out.println("orders: " + this.orderList.size());

        hasLocationPermission = false;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        markerMap = new HashMap<>();

        markerIconNormal = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        markerIconUrgent = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    }

    /**
     * Request the last known location and zoom the map to that point.
     */
    private void requestCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    this.location = location;

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
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Checks if the App has the needed permission to check the location
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

    /**
     * Once the map is ready, it will jump to the current location.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(hasLocationPermission);
        if (hasLocationPermission) {
            requestCurrentLocation();
        }
        updateMarkers();
    }

    /**
     * Displays the Recycler View with the Orders in it.
     */
    public void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    this.location = location;
                    orderAdapter = new OrderAdapter(this, (ArrayList<Order>) this.orderList, this.location);
                    recyclerView.setAdapter(orderAdapter);
                });
    }

    /**
     * Updates all markers on the map. Call this method when the orders have changed.
     */
    private void updateMarkers() {
        if (map == null) {
            return;
        }

        Set<String> oldOrderIds = new HashSet<>(markerMap.keySet());
        for (Order order : orderList) {
            Marker marker = markerMap.get(order.getId());
            if (marker == null) {
                // Add new marker
                marker = map.addMarker(new MarkerOptions()
                        .flat(true)
                        .draggable(false)
                        .icon(markerIconNormal)
                        .position(new LatLng(order.getLat(), order.getLng()))
                );
                marker.setTag(order.getId());
                markerMap.put(order.getId(), marker);
            } else {
                // Update existing marker
                marker.setPosition(new LatLng(order.getLat(), order.getLng()));
            }

            // Order is not old, no need to remove marker
            oldOrderIds.remove(order.getId());
        }

        // Remove old markers
        for (String id : oldOrderIds) {
            Marker marker = markerMap.remove(id);
            if (marker != null) {
                marker.remove();
            }
        }
    }

    private void startOrder() {
        Intent serviceIntent = new Intent(this, OrderInProgressNotification.class);
        //todo SharedPrefs
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void stopOrder() {
        Intent serviceIntent = new Intent(this, OrderInProgressNotification.class);
        stopService(serviceIntent);
    }
}

