package com.ks.einanrufhilft.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.*;

import com.passbase.passbase_sdk.Passbase;
import com.passbase.passbase_sdk.PassbaseButton;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class RegisterActivity extends AppCompatActivity {

    private Context context;
    private boolean trusted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Passbase passbaseRef = new Passbase(this);
        passbaseRef.initialize(
                "3e27309be36f707c9fea64ef81f22d011ed52942952b9e96cb5e5eff7db2c13e",
                "",
                new Pair[]{}
        );

        context = this;

        // UI elements
        Button btnBack = findViewById(R.id.registerBtnBack);
        Button btnSend = findViewById(R.id.registerBtnSend);
        ImageButton btnAddress = findViewById(R.id.registerBtnAdress);
        PassbaseButton verificationButton = this.findViewById(R.id.passbaseVerificationButton);

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

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passbaseRef.startVerification();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trusted) {
                    // TODO Send code for phone verification
                } else {

                }
            }
        });

        // Callbacks for verification
        // Add here the callbacks
        passbaseRef.onCancelPassbaseVerification(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Toast t = Toast.makeText(context, "Verifikation fehlgeschlagen!", Toast.LENGTH_LONG);
                t.show();
                return null;
            }
        });

        passbaseRef.onCompletePassbaseVerification(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String authKey) {
                Toast t1 = Toast.makeText(context, "Verifikation erfolgreich!", Toast.LENGTH_LONG);
                t1.show();
                trusted = true;

                return null;
            }
        });
    }
}
