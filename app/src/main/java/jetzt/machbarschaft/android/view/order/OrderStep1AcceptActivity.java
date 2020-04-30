package jetzt.machbarschaft.android.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.util.OrderUtil;
import jetzt.machbarschaft.android.util.ReportProblemUtil;

/**
 * Handles the case when someone accepts a order.
 */
public class OrderStep1AcceptActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step_1_accept);

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
        toolbar.setNavigationOnClickListener(v -> OrderUtil.cancelOrder(mOrder, getApplicationContext()));
        //toolbar.getNavigationIcon().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        // Button click handlers
        btnCall.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderStep2CarryOutActivity.class));
            startActivity(ReportProblemUtil.callUser(mOrder));
            finishAfterTransition();
        });

        Button btnCancel = findViewById(R.id.order_accept_button_cancel);
        btnCancel.setOnClickListener(v->{
                new
                        AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                        .setTitle("Auftrag abbrechen")
                        .setMessage("Willst du wirklich den Auftrag abbrechen?")
                        .setPositiveButton("Ja", (dialog, which) -> OrderUtil.cancelOrder(mOrder, getApplicationContext()))
                        .setNeutralButton("Fehler melden!", ((dialog, which) -> startActivity(ReportProblemUtil.getMailIntent())))
                        .setNegativeButton("Nein", (dialog, which) -> Log.wtf("Abbruch", "Cancel"))
                        .show();

        });
    }


    /**
     * Load users active order from database
     */
    private void loadOrder() {
        mOrder = Storage.getInstance().getOrderInProgress(this);
    }

}
