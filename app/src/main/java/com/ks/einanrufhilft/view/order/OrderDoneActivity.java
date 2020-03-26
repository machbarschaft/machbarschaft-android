package com.ks.einanrufhilft.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.database.Storage;
import com.ks.einanrufhilft.services.OrderInProgressNotification;

/**
 * Handles what is to do when a Order is finished.
 */
public class OrderDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_done);
        Button btnHome = findViewById(R.id.btn_back_home);
        Storage.getInstance().setActiveOrder(getApplicationContext(), false);
        Intent serviceIntent = new Intent(this, OrderInProgressNotification.class);
        stopService(serviceIntent); //stops the foregroundservice
        btnHome.setOnClickListener(v -> finish());
    }
}
