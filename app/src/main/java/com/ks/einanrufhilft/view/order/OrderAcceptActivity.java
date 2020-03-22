package com.ks.einanrufhilft.view.order;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.ks.einanrufhilft.R;

public class OrderAcceptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accept);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Button btnCall = findViewById(R.id.btn_call);
        btnCall.setOnClickListener(v -> {

        });
    }
}
