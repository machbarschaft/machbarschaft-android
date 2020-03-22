package com.ks.einanrufhilft.view.order;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.ks.einanrufhilft.R;

import java.util.Objects;

public class OrderEnRouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_en_route);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        Button btnNavigate = findViewById(R.id.btn_navigate);
        btnNavigate.setOnClickListener(v -> {

        });

        Button btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(v -> {

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO
    }
}
