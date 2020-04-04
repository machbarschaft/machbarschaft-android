package jetzt.machbarschaft.android.view.order;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.view.home.Home;

public class FirstOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_first);

        // Setup UI
        Button btn_Accept = findViewById(R.id.btn_first_order_accept);
        Button btn_Decline = findViewById(R.id.btn_first_order_decline);

        btn_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Storage.getInstance().setUserNotifyAccepted(getApplicationContext())) {
                    startHome();
                }
            }
        });

        btn_Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have to accept the Rules!", Toast.LENGTH_LONG).show();
            }
        });

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFirstOrder);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Home.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


    }

    private void startHome() {
        startActivity(new Intent(this, Home.class));
        finishAfterTransition();
    }
}
