package jetzt.machbarschaft.android.view.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crashlytics.android.Crashlytics;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.util.ReportProblemUtil;

public class OrderStep3EnRouteActivity extends AppCompatActivity {
    private static final String LOG_TAG = "OrderEnRouteActivity";

    private Order mOrder;


    @Override
    public void onBackPressed() {
        //do nothing
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step_4_en_route);

        // Get active order
        loadOrder();
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP3_EnRoute);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_order_en_route);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OrderStep2CarryOutActivity.class)));

        // Set overview label at the top of the page
        TextView tfOverview = findViewById(R.id.order_en_route_client);
        tfOverview.setText(mOrder.getClientName());

        // Set address from order to label
        TextView tfAddress = findViewById(R.id.step_3_address);
        tfAddress.setText(mOrder.getCompleteAddress());

        //Set urgency to label
        TextView tfUrgency = findViewById(R.id.step_3_urgency);
        tfUrgency.setText(mOrder.getUrgency().

                getTitle());
        if (mOrder.getUrgency() == Order.Urgency.URGENT) {
            tfUrgency.setTextColor(Color.RED);
        } else {
            tfUrgency.setTextColor(Color.BLUE);
        }

        // Button click handlers
        Button btnNavigate = findViewById(R.id.btn_navigate_step3);
        btnNavigate.setOnClickListener(v ->

                navigateToAddress());

        // Set color of icon at "navigation" button
        Drawable icon = getResources().getDrawable(android.R.drawable.ic_menu_directions, getTheme());
        icon.setColorFilter(

                getColor(R.color.order_step_2_icon), PorterDuff.Mode.SRC_IN);
        btnNavigate.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        Button btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(v ->

        {
            notifyOrderDone();
            startActivity(new Intent(this, OrderStep4DoneActivity.class));
            finishAfterTransition();
        });

        Button reportProblem = findViewById(R.id.order_en_route_btn_report);
        reportProblem.setOnClickListener(v ->

        {
            new
                    //AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)

                    AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                    .setTitle(R.string.report_problem)
                    .setMessage(R.string.report_problem_describtion)
                    .setPositiveButton(R.string.call_again, (dialog, which) -> startActivity(ReportProblemUtil.callUser(mOrder)))
                    .setNeutralButton(R.string.back_button, (dialog, which) -> Log.d("MainActivity", "do nothing"))
                    .setNegativeButton(R.string.report_problem, (dialog, which) -> startActivity(ReportProblemUtil.getMailIntent()))
                    .show();
        });
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
                mOrder.getLatitude(), mOrder.getLongitude(), mOrder.getCompleteAddress()));
        startActivity(new Intent(Intent.ACTION_VIEW, locationUri));
    }

    /**
     * Get active order from database
     */
    private void loadOrder() {
        //mOrder = Storage.getInstance().getCurrentOrder();
        mOrder = Storage.getInstance().getOrderInProgress(getApplicationContext());
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
