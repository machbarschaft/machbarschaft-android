package com.ks.einanrufhilft.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.*;

import com.passbase.passbase_sdk.Passbase;
import com.passbase.passbase_sdk.PassbaseButton;

import java.util.Random;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;


public class RegisterActivity extends AppCompatActivity {

    private Context context;
    private boolean trusted = false;
    private boolean agbAcepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        // UI elements
        Button btnBack = findViewById(R.id.registerBtnBack);
        Button btnSend = findViewById(R.id.registerBtnSend);
        ImageButton btnAddress = findViewById(R.id.registerBtnAdress);
        CheckBox agbBox = findViewById(R.id.registerCheckAGB);

        EditText tfName = findViewById(R.id.registerTfName);
        EditText tfSurname = findViewById(R.id.registerTfSurname);
        EditText tfPhone = findViewById(R.id.registerTfPhone);
        EditText tfAddress = findViewById(R.id.registerTfAddress);

        final Passbase passbaseRef = new Passbase(this);
        passbaseRef.initialize(
                "3e27309be36f707c9fea64ef81f22d011ed52942952b9e96cb5e5eff7db2c13e",
                "",
                new Pair[]{}
        );
        PassbaseButton verificationButton = this.findViewById(R.id.passbaseVerificationButton);
        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passbaseRef.startVerification();
            }
        });
        // Callbacks for verification
        // Add here the callbacks
        passbaseRef.onCancelPassbaseVerification(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Toast t = Toast.makeText(context, "Verifikation fehlgeschlagen!", Toast.LENGTH_LONG);
                t.show();
                trusted = false;

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

        // Button click handlers
        agbBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agbAcepted = ((CheckBox)view).isChecked();
            }
        });

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trusted) {
                    if(agbAcepted) {
                        String code = getCode();
                        sendSMS(tfPhone.getText().toString(), code);
                        // Transfer user data in array: phone code, name, surname, address, phone number
                        String[] data = {code, tfName.getText().toString(), tfSurname.getText().toString(), tfAddress.getText().toString(), tfPhone.getText().toString()};
                        Intent i = new Intent(context, VerifyPhoneActivity.class);
                        i.putExtra("registerData", data);
                        startActivity(i);
                    } else {
                        Toast t = Toast.makeText(context, "Bitte akzeptiere unsere AGB",  Toast.LENGTH_LONG);
                        t.show();
                    }
                } else {
                    Toast t = Toast.makeText(context, "Bitte verifiziere dich",  Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }

    private void sendSMS(String phoneNo, String code) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, code, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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
}
