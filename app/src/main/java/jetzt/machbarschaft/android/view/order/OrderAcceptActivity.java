package jetzt.machbarschaft.android.view.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;

/**
 * Handles the case when someone accepts a order.
 */
public class OrderAcceptActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accept);

        // Load active order from database
        loadOrder();
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP1_PHONE);

        // Get UI elements
        Button btnCall = findViewById(R.id.btn_call);

        TextView descriptionView = findViewById(R.id.order_accept_text);
        descriptionView.setText(getString(R.string.order_accept_text, mOrder == null ? "???" : mOrder.getClientName()));

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.order_accept_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OrderDetailActivity.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // Button click handlers
        btnCall.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderCarryOutActivity.class));
            callUser();
            finishAfterTransition();
        });
    }

    /**
     * Start phone app with number from order
     */
    private void callUser() {
        Uri callUri = Uri.parse("tel:" + (mOrder == null ? "0000000" : mOrder.getPhoneNumber()));
        startActivity(new Intent(Intent.ACTION_VIEW, callUri));
    }

    /**
     * Load users active order from database
     */
    private void loadOrder() {
        mOrder = Storage.getInstance().getOrderInProgress(this);
    }
}
