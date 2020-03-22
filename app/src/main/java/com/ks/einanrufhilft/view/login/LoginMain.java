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

        //sets the Custom Pager Adapter to display the different slides in the application
        ViewPager viewPager1 = findViewById(R.id.viewpager);
        viewPager1.setAdapter(new CustomPagerAdapter(this));

        phoneNumber = findViewById(R.id.input_phonenumber);
        loginButton = findViewById(R.id.btn_login);
        TextView registerButton = findViewById(R.id.link_signup);


        /* TODO uncomment if deploy
        if(isLoggedIn()){ //Makes sure, that you just have to login Once
            onLoginSuccess();
        }
        */

        loginButton.setEnabled(true);
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

    /**
     * Checks if the Phone Number is actually valid.
     * @return True if phone number is legit.
     */
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

    /**
     * Handles what should happen when the Login is successfully.
     * In this case it will save the status of being loggedIn in the Shared Preferences and start the Home Activity.
     */
    private void onLoginSuccess() {
            SharedPreferences userData = getApplicationContext().getSharedPreferences(ApplicationConstants.SHARED_PREF_USERDATA, 0);
            Editor editor = userData.edit();
            editor.putBoolean(ApplicationConstants.SHARED_PREF_USERDATA_LOGGEDIN, true);
            editor.apply();
            this.startActivity(new Intent(this, Home.class));
    }

    /**
     * Handles the case when the Login didn't work.
     * Right now we show a small Toast which will show that the Login wasn't successfully.
     */
    private void onLoginFailed() {
        Toast.makeText(this, "Anmelden fehlgeschlagen", Toast.LENGTH_SHORT).show();
    }

    /**
     * To check whether the user is already loggedIn or needs to be loggedIn
     * @return true if the user is already logged in.
     */
    private boolean isLoggedIn(){
        return getApplicationContext().getSharedPreferences(ApplicationConstants.SHARED_PREF_USERDATA, 0).getBoolean(ApplicationConstants.SHARED_PREF_USERDATA_LOGGEDIN, false);
    }
}
