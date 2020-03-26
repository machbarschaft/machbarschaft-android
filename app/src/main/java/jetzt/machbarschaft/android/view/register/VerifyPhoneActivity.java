package jetzt.machbarschaft.android.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.entitie.Account;
import jetzt.machbarschaft.android.view.login.LoginMain;

/**
 * For verifying via SMS.
 */
public class VerifyPhoneActivity extends AppCompatActivity {
    private String[] userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        // Get user data from calling activity
        Intent caller = getIntent();
        // Data for registration in array order: phone code, name, surname, address, phone number
        userData = caller.getStringArrayExtra("registerData");

        // Get UI elements
        Toolbar toolbar = findViewById(R.id.verify_phone_toolbar);
        Button btnSignin = findViewById(R.id.button_sign_in);
        Button btnSendCode = findViewById(R.id.verify_phone_btn_send_code);
        EditText tfCode = findViewById(R.id.edit_text_code);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));

        btnSendCode.setOnClickListener(view -> {
            String code = getCode();
            userData[0] = code;
            sendSMS(userData[4], code);
        });

        btnSignin.setOnClickListener(view -> {
            if (userData[0].equals(tfCode.getText().toString())) {
                // Add new account to firebase
                Account newUser = new Account();
                newUser.setFirst_name(userData[1]);
                newUser.setLast_name(userData[2]);
                newUser.setPhone_number(userData[4]);

                DataAccess.getInstance().createAccount(newUser);

                Intent i = new Intent(getApplicationContext(), LoginMain.class);
                startActivity(i);
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "Code ungültig", Toast.LENGTH_LONG);
                t.show();
            }
        });
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

    private void sendSMS(String phoneNo, String code) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, code, null, null);
            Toast.makeText(getApplicationContext(), "SMS gesendet",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
