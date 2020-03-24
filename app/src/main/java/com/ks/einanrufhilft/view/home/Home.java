package com.ks.einanrufhilft.view.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.tasks.Task;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.OrderHandler;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.services.OrderInProgressNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private TextView footerTextView;

    private GoogleMap map;
    private boolean hasLocationPermission;
    private FusedLocationProviderClient fusedLocationClient;
    private Map<String, Marker> markerMap;
    private BitmapDescriptor markerIconNormal;
    private BitmapDescriptor markerIconUrgent;
    private Location location;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get UI elements
        Button btnFAQ = findViewById(R.id.homeBtnFAQ);
        Button btnContact = findViewById(R.id.homeBtnContact);
        Button btnReport = findViewById(R.id.homeBtnBugReport);

        // Button action handlers
        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Wir arbeiten an einer Lösung, damit du unsere FAQ's hier bald sehen kannst.");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Verstanden",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://machbarschaft.jetzt/#contact"));
                startActivity(browserIntent);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Wir arbeiten an einer Lösung. Bis dahin, melde dein Problem doch einfach an unsere sozialen Medien");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Verstanden",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        initializeData();
        initView();
        startOrder();
        updateMarkers();

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
        sortData();
        markerMap = new HashMap<>();
        markerIconNormal = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        markerIconUrgent = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    }

    /**
     * Sorts List, so that the nearest Orders are on top.
     */
    private void sortData() {
        Collections.sort(this.orderList, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                Location location1 = new Location("");
                location1.setLatitude(o1.getLat());
                location1.setLongitude(o1.getLng());
                Location location2 = new Location("");
                location2.setLatitude(o2.getLat());
                location2.setLongitude(o2.getLng());
                LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                assert locationManager != null;
                if (hasLocationPermission) {
                    @SuppressLint("MissingPermission") Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (myLocation != null) {
                        double distance1 = myLocation.distanceTo(location1);
                        double distance2 = myLocation.distanceTo(location2);
                        if (distance1 == distance2)
                            return 0;
                        return distance1 < distance2 ? -1 : 1;
                    }
                }
                return 0; //doesn't compare in case we can't get our own position
            }
        });
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

    /**
     * Asks for the needed permission on start, otherwise we cant access the location of the user.
     * Is needed to show nearby orders.
     */
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

    /**
     * Handles the Start of Order
     */
    private void startOrder() {
        Intent serviceIntent = new Intent(this, OrderInProgressNotification.class);
        //todo handle event
        //Storage.setOrderInProgress(getApplicationContext(), );
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    /**
     * Handles the Stop of a Order
     * Possible reasons for stop is a cancel of finish of order.
     */
    private void stopOrder() {
        Intent serviceIntent = new Intent(this, OrderInProgressNotification.class);
        stopService(serviceIntent);
    }
}

