package com.ks.einanrufhilft.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.database.DataAccess;
import com.ks.einanrufhilft.util.ApplicationConstants;
import com.ks.einanrufhilft.view.home.Home;
import com.ks.einanrufhilft.view.register.RegisterActivity;


/**
 * Displays the Login Page. Handles the Input and the Login Success / Login Failed.
 * Also can redirect to the register Page.
 */
public class LoginMain extends AppCompatActivity {
    private EditText phoneNumber;
    private Button loginButton;
    private Context context;

    @Override
    public void onResume() {
        super.onResume();
        loginButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        context = getApplicationContext();
        //sets the Custom Pager Adapter to display the different slides in the application

        ViewPager introSlidesPager = findViewById(R.id.intro_slides_pager);
        TabLayout introSlidesIndicator = findViewById(R.id.intro_slides_indicator);

        introSlidesPager.setAdapter(new CustomPagerAdapter(this));
        introSlidesIndicator.setupWithViewPager(introSlidesPager, true);

        phoneNumber = findViewById(R.id.input_phone_number);
        loginButton = findViewById(R.id.btn_login);
        Button registerButton = findViewById(R.id.btn_register);


        /* TODO uncomment if deploy
        if(isLoggedIn()){ //Makes sure, that you just have to login Once
            onLoginSuccess();
        }
        */

        loginButton.setOnClickListener(v -> login());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RegisterActivity.class);
                startActivity(i);
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

            DataAccess.getInstance().getOrders();

            //TODO Firebase Logic here

            new Handler().postDelayed(() -> {
                        onLoginSuccess();
                        //onLoginFailed();

                        progressDialog.dismiss();
                    }, 3000
            );

        } else {
            onLoginFailed();
        }
    }

    /**
     * Checks if the Phone Number is actually valid.
     *
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
        editor.putBoolean(ApplicationConstants.SHARED_PREF_USERDATA_LOGGED_IN, true);
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
     *
     * @return true if the user is already logged in.
     */
    private boolean isLoggedIn() {
        return getApplicationContext().getSharedPreferences(ApplicationConstants.SHARED_PREF_USERDATA, 0).getBoolean(ApplicationConstants.SHARED_PREF_USERDATA_LOGGED_IN, false);
    }
}
