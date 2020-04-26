package jetzt.machbarschaft.android.view.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.services.OrderInProgressNotification;
import jetzt.machbarschaft.android.util.FeedbackMailUtil;
import jetzt.machbarschaft.android.view.home.Home;

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

        Button btnCancel = findViewById(R.id.order_accept_button_cancel);
        btnCancel.setOnClickListener(v->{
                new
                        AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                        .setTitle("Auftrag abbrechen")
                        .setMessage("Willst du wirklich den Auftrag abbrechen?")
                        .setPositiveButton("Ja", (dialog, which) -> cancelOrder())
                        .setNeutralButton("Fehler melden!", ((dialog, which) -> startActivity(FeedbackMailUtil.getMailIntent())))
                        .setNegativeButton("Nein", (dialog, which) -> Log.wtf("Abbruch", "Cancel"))
                        .show();

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

    /**
     *  Cancels the current order.
     *  It will be opened again the Database and displayed to other helpers.
     *  Also the notification will disappear and the current steps will be reset in the storage.
     */
    private void cancelOrder(){
        DataAccess.getInstance().setOrderStatus(mOrder.getId(), Order.Status.OPEN);
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP0_NONE);
        Storage.getInstance().setActiveOrder(getApplicationContext(), false);
        Intent serviceIntent = new Intent(this, OrderInProgressNotification.class);
        stopService(serviceIntent); //stops the foregroundservice
        startActivity(new Intent(this, Home.class));
    }
}
