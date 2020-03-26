package com.ks.einanrufhilft.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.database.Storage;
import com.ks.einanrufhilft.database.entitie.Order;

public class OrderCarryOutActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_carry_out);

        loadOrder();

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
    }

    private void loadOrder() {
        // mOrder = Storage.getInstance().getCurrentOrder();
        mOrder = Storage.getInstance().getOrderInProgress(getApplicationContext());
    }
}
