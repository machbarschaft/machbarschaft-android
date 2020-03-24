package com.ks.einanrufhilft.view.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ks.einanrufhilft.Database.DataAccess;
import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.Storage;
import com.ks.einanrufhilft.R;

public class OrderEnRouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = "OrderEnRouteActivity";

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_en_route);

        loadOrder();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
*/
        Button btnNavigate = findViewById(R.id.btn_navigate);
        btnNavigate.setOnClickListener(v -> navigateToAddress());

        Button btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(v -> {
            notifyOrderDone();
            startActivity(new Intent(this, OrderDoneActivity.class));
            finishAfterTransition();
        });
    }

    private void navigateToAddress() {
        if (mOrder == null) {
            return;
        }

        @SuppressLint("DefaultLocale")
        Uri locationUri = Uri.parse(String.format("geo:%f,%f?q=%s",
                mOrder.getLatitude(), mOrder.getLongitude(), mOrder.getClientName()));
        startActivity(new Intent(Intent.ACTION_VIEW, locationUri));
    }

    private void loadOrder() {
        mOrder = Storage.getInstance().getCurrentOrder();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mOrder != null) {
            LatLng orderLocation = new LatLng(mOrder.getLatitude(), mOrder.getLongitude());
            // Add marker
            googleMap.addMarker(new MarkerOptions()
                    .flat(true)
                    .draggable(false)
                    .position(orderLocation));

            // Zoom map
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orderLocation, 5f));
        }
    }

    /**
     * Tell the server that the order is done.
     */
    private void notifyOrderDone() {
        Database database = Database.getInstance();
        try {
            DataAccess.getInstance().setOrderStatus(mOrder.getId(), DataAccess.Status.Closed);
        } catch (Exception exception) {
            Log.e(LOG_TAG, "Failed to update order status!", exception);
        }
    }
}
