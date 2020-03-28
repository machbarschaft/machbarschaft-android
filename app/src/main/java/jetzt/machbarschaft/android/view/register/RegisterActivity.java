package jetzt.machbarschaft.android.view.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.passbase.passbase_sdk.Passbase;
import com.passbase.passbase_sdk.PassbaseButton;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.entitie.Account;
import jetzt.machbarschaft.android.view.login.LoginMain;
import jetzt.machbarschaft.android.view.register.sms.SMSData;
import jetzt.machbarschaft.android.view.register.sms.SMSManager;
import kotlin.Pair;

/**
 * Handles the Events to register. Includes the Passbase Video Ident and the registering with mobile number.
 */
public class RegisterActivity extends AppCompatActivity {
    private boolean trusted = false;
    private boolean agbAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI elements
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        Button btnSend = findViewById(R.id.register_btn_send);
        CheckBox agbBox = findViewById(R.id.register_check_agb);

        EditText tfPhone = findViewById(R.id.register_tf_phone);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginMain.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        final Passbase passbaseRef = new Passbase(this);
        passbaseRef.initialize(
                "3e27309be36f707c9fea64ef81f22d011ed52942952b9e96cb5e5eff7db2c13e",
                "",
                new Pair[]{}
        );
        PassbaseButton verificationButton = findViewById(R.id.passbase_verification_button);
        verificationButton.setOnClickListener(view -> passbaseRef.startVerification());
        // Callbacks for verification
        // Add here the callbacks
        passbaseRef.onCancelPassbaseVerification(() -> {
            Toast.makeText(this, R.string.verify_error_generic, Toast.LENGTH_LONG).show();
            trusted = false;

            return null;
        });

        passbaseRef.onCompletePassbaseVerification(authKey -> {
            Toast.makeText(this, R.string.verify_success, Toast.LENGTH_LONG).show();
            trusted = true;

            return null;
        });

        // Button click handlers
        agbBox.setOnClickListener(view -> agbAccepted = ((CheckBox) view).isChecked());

        // TODO Get user information for new account from passbase key
        /*
        btnSend.setOnClickListener(view -> {
            if (tfName.getText().toString().isEmpty() || tfSurname.getText().toString().isEmpty() || tfAddress.getText().toString().isEmpty() || tfPhone.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.verify_error_fill_all_fields, Toast.LENGTH_LONG).show();
            } else {
                if (trusted) {
                    if (agbAccepted) {
                        Account account = new Account(tfName.getText().toString(), tfSurname.getText().toString(),tfPhone.getText().toString(),0);
                        SMSManager.getInstance().sendSMS(new SMSData(account),this);
                    } else {
                        Toast.makeText(this, R.string.verify_error_no_agb, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.verify_error_verify, Toast.LENGTH_LONG).show();
                }
            }
        });
        */
    }



}
