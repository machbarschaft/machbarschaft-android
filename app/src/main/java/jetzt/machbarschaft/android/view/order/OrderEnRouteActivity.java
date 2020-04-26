package jetzt.machbarschaft.android.view.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rd.PageIndicatorView;

import java.util.Objects;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;

public class OrderEnRouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = "OrderEnRouteActivity";

    private Order mOrder;


    @Override
    public void onBackPressed() {
            //do nothing
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_en_route);

        // Get active order
        loadOrder();
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP3_EnRoute);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbarRouteOrder);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OrderCarryOutActivity.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // Set overview label at the top of the page
        TextView tfOverview = findViewById(R.id.step_3_overview);
        tfOverview.setText(mOrder.getType_of_help() + " " + getResources().getString(R.string.stepFor) + " " + mOrder.getClientName());

        // Set address from order to label
        TextView tfAddress = findViewById(R.id.step_3_address);
        tfAddress.setText(mOrder.getCompleteAddress());

        //Setup Page Indicator to show progress Step 3
        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(3); // specify total count of indicators
        pageIndicatorView.setSelection(2);

        // Load map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        // Button click handlers
        Button btnNavigate = findViewById(R.id.btn_navigate);
        btnNavigate.setOnClickListener(v -> navigateToAddress());

        // Set color of icon at "navigation" button
        Drawable icon = getResources().getDrawable(android.R.drawable.ic_menu_directions, getTheme());
        icon.setColorFilter(getColor(R.color.order_step_2_icon), PorterDuff.Mode.SRC_IN);
        btnNavigate.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        Button btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(v -> {
            notifyOrderDone();
            startActivity(new Intent(this, OrderDoneActivity.class));
            finishAfterTransition();
        });

        Button btnCall = findViewById(R.id.step_3_btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUser();
            }
        });
    }

    /**
     * Open phone app with number from order
     */
    private void callUser() {
        Uri callUri = Uri.parse("tel:" + (mOrder == null ? "0000000" : mOrder.getPhoneNumber()));
        startActivity(new Intent(Intent.ACTION_VIEW, callUri));
    }

    /**
     * Start maps app to navigate to order address
     */
    private void navigateToAddress() {
        if (mOrder == null) {
            return;
        }

        @SuppressLint("DefaultLocale")
        Uri locationUri = Uri.parse(String.format("geo:%f,%f?q=%s",
                mOrder.getLatitude(), mOrder.getLongitude(), mOrder.getClientName()));
        startActivity(new Intent(Intent.ACTION_VIEW, locationUri));
    }

    /**
     * Get active order from database
     */
    private void loadOrder() {
        //mOrder = Storage.getInstance().getCurrentOrder();
        mOrder = Storage.getInstance().getOrderInProgress(getApplicationContext());
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
        try {
            DataAccess.getInstance().setOrderStatus(mOrder.getId(), Order.Status.CLOSED);
        } catch (Exception exception) {
            Log.e(LOG_TAG, "Failed to update order status!", exception);
            Crashlytics.logException(exception);
        }
    }
}
