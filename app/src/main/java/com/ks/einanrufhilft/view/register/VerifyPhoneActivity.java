package com.ks.einanrufhilft.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.Database.Entitie.Account;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.LoginMain;

public class VerifyPhoneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        // Get user data from calling activity
        Intent caller = getIntent();
        // Data for registration in array order: phone code, name, surname, address, phone number
        String[] userData = caller.getStringArrayExtra("registerData");

        // Get UI elements
        Button btnSignin = findViewById(R.id.buttonSignIn);
        EditText tfCode = findViewById(R.id.editTextCode);

        // Button click handlers
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userData[0].equals(tfCode.getText().toString())) {
                    // Add new account to firebase
                    Account newUser = new Account();
                    newUser.setFirst_name(userData[1]);
                    newUser.setLast_name(userData[2]);
                    newUser.setPhone_number(userData[4]);

                    Database.getInstance().createAccount(newUser);

                    Intent i = new Intent(getApplicationContext(), LoginMain.class);
                    startActivity(i);
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Code ungültig", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });


    }
}
