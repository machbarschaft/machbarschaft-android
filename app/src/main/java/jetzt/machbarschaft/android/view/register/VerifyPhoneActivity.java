package jetzt.machbarschaft.android.view.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.view.register.sms.SMSData;
import jetzt.machbarschaft.android.view.register.sms.SMSManager;

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

        // Get UI elements
        Toolbar toolbar = findViewById(R.id.verify_phone_toolbar);
        Button btnSignIn = findViewById(R.id.button_sign_in);
        Button btnSendCode = findViewById(R.id.verify_phone_btn_send_code);
        EditText tfCode1 = findViewById(R.id.verificationTfCode1);
        EditText tfCode2 = findViewById(R.id.verificationTfCode2);
        EditText tfCode3 = findViewById(R.id.verificationTfCode3);
        EditText tfCode4 = findViewById(R.id.verificationTfCode4);
        EditText tfCode5 = findViewById(R.id.verificationTfCode5);
        EditText tfCode6 = findViewById(R.id.verificationTfCode6);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        SMSData smsData = (SMSData) caller.getSerializableExtra("smsData");
        SMSManager smsManager = SMSManager.getInstance();

        btnSendCode.setOnClickListener(view -> {
            smsManager.sendSMS(smsData,this);
        });

        btnSignIn.setOnClickListener(view -> {
            // Combine user input of all textfields
            String code = tfCode1.getText().toString()+tfCode2.getText().toString()+tfCode3.getText().toString()+
                    tfCode4.getText().toString()+tfCode5.getText().toString()+tfCode6.getText().toString();
            //System.out.println("*********" + code + "*********");
            smsManager.verifySmsCode(smsData,code,this);
        });

        // Focus handlers
        tfCode1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(tfCode1.getText().length() == 1)
                    tfCode2.requestFocus();
                return false;
            }
        });
        tfCode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(tfCode2.getText().length() == 1)
                    tfCode3.requestFocus();
                return false;
            }
        });
        tfCode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(tfCode3.getText().length() == 1)
                    tfCode4.requestFocus();
                return false;
            }
        });
        tfCode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(tfCode4.getText().length() == 1)
                    tfCode5.requestFocus();
                return false;
            }
        });
        tfCode5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(tfCode5.getText().length() == 1)
                    tfCode6.requestFocus();
                return false;
            }
        });
    }

}