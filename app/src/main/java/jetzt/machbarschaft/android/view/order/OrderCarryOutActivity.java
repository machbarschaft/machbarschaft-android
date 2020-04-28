package jetzt.machbarschaft.android.view.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.util.ReportProblemUtil;

public class OrderCarryOutActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    public void onBackPressed() {
            //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_carry_out);

        // Get UI elements
        Button btnStartNow = findViewById(R.id.btn_order_execute_now);
        Button btnStartLater = findViewById(R.id.btn_order_execute_later);
        Button btnStartFailed = findViewById(R.id.btn_order_step_2_report_problem);

        // Load active order from Database
        loadOrder();
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP2_CarryOut);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_order_carry_out);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OrderAcceptActivity.class)));
        //toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // Button click handlers
        btnStartNow.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderEnRouteActivity.class));
            finishAfterTransition();
        });



        btnStartLater.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderEnRouteActivity.class));
            finishAfterTransition();
        });

        btnStartFailed.setOnClickListener(v -> {
            new
                    AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                    .setTitle(R.string.report_problem)
                    .setMessage(R.string.report_problem_describtion)
                    .setPositiveButton(R.string.call_again, (dialog, which) -> startActivity(ReportProblemUtil.callUser(mOrder)))
                    .setNeutralButton(R.string.back_button, (dialog, which) -> Log.d("MainActivity", "do nothing") )
                    .setNegativeButton(R.string.report_problem, (dialog, which) -> startActivity(ReportProblemUtil.getMailIntent()))
                    .show();
        });
    }

    /**
     * Load users active order from database
     */
    private void loadOrder() {
        // mOrder = Storage.getInstance().getCurrentOrder();
        mOrder = Storage.getInstance().getOrderInProgress(getApplicationContext());
    }
}
