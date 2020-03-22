package com.ks.einanrufhilft.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ks.einanrufhilft.R;

public class VerifyPhoneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        // Get user data from calling activity
        Intent caller = getIntent();
        // Data for registration in array order: name, surname, address, phone number
        String[] userData = caller.getStringArrayExtra("registerData");

        // Get UI elements
        Button btnSignin = findViewById(R.id.buttonSignIn);

        // Button click handlers
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Check for correct code
                // TODO Add account to firebase
            }
        });


    }
}
