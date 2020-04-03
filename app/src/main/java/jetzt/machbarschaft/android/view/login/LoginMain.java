package jetzt.machbarschaft.android.view.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.util.ApplicationConstants;
import jetzt.machbarschaft.android.view.home.Home;
import jetzt.machbarschaft.android.view.register.RegisterActivity;
import jetzt.machbarschaft.android.view.register.VerifyPhoneActivity;
import jetzt.machbarschaft.android.view.register.sms.SMSData;
import jetzt.machbarschaft.android.view.register.sms.SMSEventListenerImpl;
import jetzt.machbarschaft.android.view.register.sms.SMSManager;


/**
 * Displays the Login Page. Handles the Input and the Login Success / Login Failed.
 * Also can redirect to the register Page.
 */
public class LoginMain extends AppCompatActivity {
    private EditText phoneNumber;
    private Button loginButton;
    private Context context;
    private ProgressDialog progressDialog;
    private AutoCompleteTextView countryCodeTextView;

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

        // Sets the Custom Pager Adapter to display the different slides in the application
        ViewPager introSlidesPager = findViewById(R.id.intro_slides_pager);
        TabLayout introSlidesIndicator = findViewById(R.id.intro_slides_indicator);

        introSlidesPager.setAdapter(new CustomPagerAdapter(this));
        introSlidesIndicator.setupWithViewPager(introSlidesPager, true);

        // Get UI elements
        phoneNumber = findViewById(R.id.input_phone_number);
        loginButton = findViewById(R.id.btn_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_in_progress));

        // Button click handlers
        loginButton.setOnClickListener(v -> login());

        String[] countryCodes = getResources().getStringArray(R.array.countryCode_spinner_array);
        ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, countryCodes);
        countryCodeTextView =
                findViewById(android.R.id.content)
                        .findViewById(R.id.filled_exposed_dropdown);
        countryCodeTextView.setText(countryCodes[0], false);
        countryCodeTextView.setAdapter(countryCodeAdapter);
    }

    public void login() {
        if (validate()) {
            loginButton.setEnabled(false);
            progressDialog.show();
            DataAccess.getInstance().existUser(getPhoneNumber(), exist -> {
                if (exist) {
                    SMSManager.getInstance().sendSMS(new SMSData(getPhoneNumber()), this, new SMSEventListenerImpl() {
                        @Override
                        public void onNumberWrongFormatted(Exception firebaseException, Activity activity) {
                            progressDialog.dismiss();
                            loginButton.setEnabled(true);
                            onLoginFailed();
                        }

                        @Override
                        public void onError(Exception firebaseException, Activity activity) {
                            onErrorWhileLoading();
                            onLoginFailed();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), R.string.login_error_number_not_exist, Toast.LENGTH_SHORT).show();
                    onErrorWhileLoading();
                }
            });


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

        String phoneNumberStr = phoneNumber.getText().toString();

        if (phoneNumberStr.isEmpty() || !Patterns.PHONE.matcher(phoneNumberStr).matches()) {
            phoneNumber.setError(getString(R.string.login_error_invalid_phone_number));
            valid = false;
        } else {
            phoneNumber.setError(null);
        }

        return valid;
    }

    private void onErrorWhileLoading() {
        loginButton.setEnabled(true);
        progressDialog.dismiss();
    }

    /**
     * Handles the case when the Login didn't work.
     * Right now we show a small Toast which will show that the Login wasn't successfully.
     */
    private void onLoginFailed() {
        Toast.makeText(getApplicationContext(), R.string.login_error_generic, Toast.LENGTH_SHORT).show();
    }

    /**
     * Adds the Country Code to the Phone Number and removes unnecessary Zeros in Front of the Number,
     * in case the user doesn't know, that there should not follow a Zero directly after Country Code.
     * Also removes Whitespaces with Regex inside of the number.
     * Accepts Phone Numbers in Style of: 0176 11111111, 176 11111111, 17611111111, 176 1111 1111
     * So Phone Numbers will uniform like: <br>
     * +49176222222
     *
     * @return phone Number
     */
    public String getPhoneNumber() {
        String secondPartNumber = phoneNumber.getText().toString();
        return countryCodeTextView.getText().toString() + secondPartNumber.replaceAll("^0+", "").replaceAll("\\s+", "");
    }
}
