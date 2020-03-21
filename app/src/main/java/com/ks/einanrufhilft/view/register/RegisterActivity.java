package com.ks.einanrufhilft.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.*;

public class RegisterActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        // UI elements
        Button btnBack = findViewById(R.id.registerBtnBack);
        Button btnSend = findViewById(R.id.registerBtnSend);
        ImageButton btnAddress = findViewById(R.id.registerBtnAdress);
        ImageButton btnIdentHelp = findViewById(R.id.registerBtnIdentHelp);

        // Button click handlers
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LoginMain.class);
                startActivity(i);
            }
        });

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Open Google Maps and let user select address
            }
        });

        btnIdentHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Popup/page with information for IDENT
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Register user data in firebase
            }
        });
    }
}
