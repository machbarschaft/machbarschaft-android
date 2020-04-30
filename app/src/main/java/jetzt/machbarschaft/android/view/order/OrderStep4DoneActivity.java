package jetzt.machbarschaft.android.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;
import jetzt.machbarschaft.android.services.ActiveOrderService;
import jetzt.machbarschaft.android.view.home.HomeActivity;

/**
 * Handles what is to do when a Order is finished.
 */
public class OrderStep4DoneActivity extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        //do nothing
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step_4_done);

        // Get UI elements
        Button btnHome = findViewById(R.id.btn_back_home);

        // Set order status
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP0_NONE);
        Storage.getInstance().setActiveOrder(getApplicationContext(), false);

        Intent serviceIntent = new Intent(this, ActiveOrderService.class);
        stopService(serviceIntent); //stops the foregroundservice

        // Button click handlers
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }
}
