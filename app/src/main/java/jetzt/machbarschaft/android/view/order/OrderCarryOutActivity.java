package jetzt.machbarschaft.android.view.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderSteps;

public class OrderCarryOutActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_carry_out);

        loadOrder();
        Storage.getInstance().setCurrentStep(getApplicationContext(), OrderSteps.STEP2_CarryOut);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnStartNow = findViewById(R.id.btn_order_execute_now);
        btnStartNow.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderEnRouteActivity.class));
            finishAfterTransition();
        });

        Button btnStartLater = findViewById(R.id.btn_order_execute_later);
        btnStartLater.setOnClickListener(v -> {

        });

        Button btnStartFailed = findViewById(R.id.btn_order_execute_failed);
        btnStartFailed.setOnClickListener(v -> {

        });

        TextView tfOverview = findViewById(R.id.step_2_overview);
        tfOverview.setText(mOrder.getType_of_help().getName() + R.string.stepFor + mOrder.getClientName());

        Button btnCall = findViewById(R.id.step_2_btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUser();
            }
        });
    }

    private void loadOrder() {
        // mOrder = Storage.getInstance().getCurrentOrder();
        mOrder = Storage.getInstance().getOrderInProgress(getApplicationContext());
    }

    private void callUser() {
        Uri callUri = Uri.parse("tel:" + (mOrder == null ? "0000000" : mOrder.getPhoneNumber()));
        startActivity(new Intent(Intent.ACTION_VIEW, callUri));
    }
}
