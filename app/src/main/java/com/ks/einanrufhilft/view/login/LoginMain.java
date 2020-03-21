package com.ks.einanrufhilft.view.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ks.einanrufhilft.util.ApplicationConstants;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.home.Home;

public class LoginMain extends AppCompatActivity {

    private static final int HANDLER_SIGNUP = 0;

    private EditText phoneNumber;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        ViewPager viewPager1 = findViewById(R.id.viewpager);
        viewPager1.setAdapter(new CustomPagerAdapter(this));

        phoneNumber = findViewById(R.id.input_phonenumber);
        loginButton = findViewById(R.id.btn_login);
        TextView registerButton = findViewById(R.id.link_signup);

        if(isLoggedIn()){
            onLoginSuccess();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Register
            }
        });
    }


    public void login() {
        if (validate()) {
            loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginMain.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Anmeldung...");
            progressDialog.show();

            String phoneNumberStr = phoneNumber.getText().toString();

            //TODO Firebase Logic here

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {

                            onLoginSuccess();
                            //onLoginFailed();

                            progressDialog.dismiss();
                        }
                    }, 3000
            );

        } else {
            onLoginFailed();
        }
    }


    public boolean validate() {
        boolean valid = true;

        String phoneNumberStr = this.phoneNumber.getText().toString();

        if (phoneNumberStr.isEmpty() || !Patterns.PHONE.matcher(phoneNumberStr).matches()) {
            phoneNumber.setError("Geben Sie eine valide Telefonnummer ein!");
            valid = false;
        } else {
            phoneNumber.setError(null);
        }

        return valid;
    }

    private void onLoginSuccess() {
            SharedPreferences userData = getApplicationContext().getSharedPreferences(ApplicationConstants.SHARED_PREF_USERDATA, 0);
            Editor editor = userData.edit();
            editor.putBoolean(ApplicationConstants.SHARED_PREF_USERDATA_LOGGEDIN, true);
            editor.apply();
            this.startActivity(new Intent(this, Home.class));
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Anmelden fehlgeschlagen", Toast.LENGTH_SHORT).show();
    }

    private boolean isLoggedIn(){
        return getApplicationContext().getSharedPreferences(ApplicationConstants.SHARED_PREF_USERDATA, 0).getBoolean(ApplicationConstants.SHARED_PREF_USERDATA_LOGGEDIN, false);
    }
}
