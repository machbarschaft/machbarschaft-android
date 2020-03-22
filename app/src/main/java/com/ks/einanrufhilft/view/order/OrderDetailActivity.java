package com.ks.einanrufhilft.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.R;

public class OrderDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ORDER_ID = "orderId";

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        loadOrder();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView nameView = findViewById(R.id.order_detail_name);
        TextView needsView = findViewById(R.id.order_detail_needs);
        TextView urgencyView = findViewById(R.id.order_detail_urgency);
        TextView addressView = findViewById(R.id.order_detail_address);
    }

    private void loadOrder() {
        Intent intent = getIntent();
        if (intent != null) {
            final String orderId = intent.getStringExtra(EXTRA_ORDER_ID);

            Database database = Database.getInstance();
            mOrder = database.getOrder(orderId);
        }
    }
}
