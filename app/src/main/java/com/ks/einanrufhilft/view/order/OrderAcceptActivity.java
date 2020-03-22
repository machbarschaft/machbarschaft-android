package com.ks.einanrufhilft.view.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.Storage;
import com.ks.einanrufhilft.R;

public class OrderAcceptActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accept);

        loadOrder();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView descriptionView = findViewById(R.id.order_accept_text);
        descriptionView.setText(getString(R.string.order_accept_text, mOrder == null ? "???" : mOrder.getName()));

        Button btnCall = findViewById(R.id.btn_call);
        btnCall.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderCarryOutActivity.class));
            callUser();
            finishAfterTransition();
        });
    }

    private void callUser() {
        Uri callUri = Uri.parse("tel:" + (mOrder == null ? "0000000" : mOrder.getPhone_number()));
        startActivity(new Intent(Intent.ACTION_VIEW, callUri));
    }

    private void loadOrder() {
        mOrder = Storage.getInstance().getCurrentOrder();
    }
}
