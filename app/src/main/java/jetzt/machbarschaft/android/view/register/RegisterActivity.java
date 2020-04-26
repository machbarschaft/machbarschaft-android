package jetzt.machbarschaft.android.view.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.view.login.LoginMain;

/**
 * Handles the Events to register. Includes the registration with mobile number.
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
        EditText phoneSecondPart = findViewById(R.id.register_tf_phone2);

        //Setup Spinner Menu
        String[] countryCodes = getResources().getStringArray(R.array.country_codes);
        ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, countryCodes);
        AutoCompleteTextView countryCodeTextView = findViewById(android.R.id.content)
                .findViewById(R.id.filled_exposed_dropdown_country_Code_Register);
        countryCodeTextView.setText(countryCodes[0], false);
        countryCodeTextView.setAdapter(countryCodeAdapter);

        // Setup toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginMain.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // Button click handlers
        agbBox.setOnClickListener(view -> agbAccepted = ((CheckBox) view).isChecked());
/*
        btnSend.setOnClickListener(view -> {
            if (tfName.getText().toString().isEmpty() || tfSurname.getText().toString().isEmpty() || tfAddress.getText().toString().isEmpty() || PhoneNumberFormatterUtil.getPhoneNumber(countryCodeTextView.getText().toString(), phoneSecondPart.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.verify_error_fill_all_fields, Toast.LENGTH_LONG).show();
            } else {
                if (trusted) {
                    if (agbAccepted) {
                        Account account = new Account(tfName.getText().toString(), tfSurname.getText().toString(),PhoneNumberFormatterUtil.getPhoneNumber(countryCodeTextView.getText().toString(), phoneSecondPart.getText().toString()),0);
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
