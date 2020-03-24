package com.ks.einanrufhilft.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.Database.Entitie.Account;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.LoginMain;

import java.util.Random;

/**
 * For verifying via SMS.
 */
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
        Button btnSendCode = findViewById(R.id.verifyPhoneBtnSendCode);
        ImageButton btnBack = findViewById(R.id.verificationBtnBack);
        EditText tfCode = findViewById(R.id.editTextCode);

        // Button click handlers
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = getCode();
                userData[0] = code;
                sendSMS(userData[4], code);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert userData != null;
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

    private String getCode() {
        Random r = new Random();
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        String result = "";
        for(int i=0; i<4; i++) {
            result = result + array[r.nextInt(9)];
        }

        return result;
    }

    private void sendSMS(String phoneNo, String code) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, code, null, null);
            Toast.makeText(getApplicationContext(), "SMS gesendet",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
