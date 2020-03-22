package com.ks.einanrufhilft.view.order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toolbar;

import com.ks.einanrufhilft.R;

public class OrderCarryOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_carry_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Button btnStartNow = findViewById(R.id.btn_order_execute_now);
        btnStartNow.setOnClickListener(v -> {

        });

        Button btnStartLater = findViewById(R.id.btn_order_execute_later);
        btnStartLater.setOnClickListener(v -> {

        });

        Button btnStartFailed = findViewById(R.id.btn_order_execute_failed);
        btnStartFailed.setOnClickListener(v -> {

        });
    }
}
