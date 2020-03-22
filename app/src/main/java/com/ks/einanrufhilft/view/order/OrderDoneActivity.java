package com.ks.einanrufhilft.view.order;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ks.einanrufhilft.R;

/**
 * Handles what is to do when a Order is finished.
 */
public class OrderDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_done);

        Button btnHome = findViewById(R.id.btn_back_home);
        btnHome.setOnClickListener(v -> finish());
    }
}
