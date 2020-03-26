package com.ks.einanrufhilft.view.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.LoginMain;
import com.passbase.passbase_sdk.Passbase;
import com.passbase.passbase_sdk.PassbaseButton;

import java.util.Random;

import kotlin.Pair;

/**
 * Handles the Events to register. Includes the Passbase Video Ident and the registering with mobile number.
 */
public class RegisterActivity extends AppCompatActivity {

    private Context context;
    private boolean trusted = false;
    private boolean agbAccepted = false;
    private boolean codeSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        // UI elements
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        Button btnSend = findViewById(R.id.register_btn_send);
        ImageButton btnAddress = findViewById(R.id.register_btn_address);
        CheckBox agbBox = findViewById(R.id.register_check_agb);

        EditText tfName = findViewById(R.id.register_tf_name);
        EditText tfSurname = findViewById(R.id.register_tf_surname);
        EditText tfPhone = findViewById(R.id.register_tf_phone);
        EditText tfAddress = findViewById(R.id.register_tf_address);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginMain.class)));

        final Passbase passbaseRef = new Passbase(this);
        passbaseRef.initialize(
                "3e27309be36f707c9fea64ef81f22d011ed52942952b9e96cb5e5eff7db2c13e",
                "",
                new Pair[]{}
        );
        PassbaseButton verificationButton = this.findViewById(R.id.passbase_verification_button);
        verificationButton.setOnClickListener(view -> passbaseRef.startVerification());
        // Callbacks for verification
        // Add here the callbacks
        passbaseRef.onCancelPassbaseVerification(() -> {
            Toast t = Toast.makeText(context, "Verifikation fehlgeschlagen!", Toast.LENGTH_LONG);
            t.show();
            trusted = false;

            return null;
        });

        passbaseRef.onCompletePassbaseVerification(authKey -> {
            Toast t1 = Toast.makeText(context, "Verifikation erfolgreich!", Toast.LENGTH_LONG);
            t1.show();
            trusted = true;

            return null;
        });

        // Button click handlers
        agbBox.setOnClickListener(view -> agbAccepted = ((CheckBox) view).isChecked());

        btnAddress.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Wir arbeiten daran, dass du deine Adresse bald über Maps suchen kannst.");
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "Verstanden",
                    (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
            // TODO Open Google Maps and let user select address
        });

        btnSend.setOnClickListener(view -> {
            if (tfName.getText().toString().isEmpty() || tfSurname.getText().toString().isEmpty() || tfAddress.getText().toString().isEmpty() || tfPhone.getText().toString().isEmpty()) {
                Toast t = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_LONG);
                t.show();
            } else {
                if (trusted) {
                    if (agbAccepted) {
                        String code = getCode();
                        sendSMS(tfPhone.getText().toString(), code);
                        if (codeSend) {
                            // Transfer user data in array: phone code, name, surname, address, phone number
                            String[] data = {code, tfName.getText().toString(), tfSurname.getText().toString(), tfAddress.getText().toString(), tfPhone.getText().toString()};
                            Intent i = new Intent(context, VerifyPhoneActivity.class);
                            i.putExtra("registerData", data);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "SMS konnte nicht gesendet werden", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast t = Toast.makeText(context, "Bitte akzeptiere unsere AGB", Toast.LENGTH_LONG);
                        t.show();
                    }
                } else {
                    Toast t = Toast.makeText(context, "Bitte verifiziere dich", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }

    private void sendSMS(String phoneNo, String code) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, code, null, null);
            Toast.makeText(getApplicationContext(), "SMS gesendet", Toast.LENGTH_LONG).show();
            codeSend = true;
        } catch (Exception ex) {
            codeSend = false;
        }
    }

    private String getCode() {
        Random r = new Random();
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            result.append(array[r.nextInt(9)]);
        }

        return result.toString();
    }
}
