package jetzt.machbarschaft.android.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.util.PhoneNumberFormatterUtil;
import jetzt.machbarschaft.android.view.register.VerifyPhoneActivity;


/**
 * Displays the Login Page. Handles the Input and the Login Success / Login Failed.
 * Also can redirect to the register Page.
 */
public class LoginMain extends AppCompatActivity {
    private EditText phoneNumberTextView;
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
        phoneNumberTextView = findViewById(R.id.input_phone_number);
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
                        .findViewById(R.id.filled_exposed_dropdown_country_Code_login);
        countryCodeTextView.setText(countryCodes[0], false);
        countryCodeTextView.setAdapter(countryCodeAdapter);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        progressDialog.show();

        String phoneNumber = PhoneNumberFormatterUtil.getPhoneNumber(
                countryCodeTextView.getText().toString(), phoneNumberTextView.getText().toString());
        startActivity(new Intent(this, VerifyPhoneActivity.class)
                .putExtra(VerifyPhoneActivity.EXTRA_PHONE_NUMBER, phoneNumber));
    }

    /**
     * Checks if the Phone Number is actually valid.
     *
     * @return True if phone number is legit.
     */
    public boolean validate() {
        boolean valid = true;

        String phoneNumberStr = phoneNumberTextView.getText().toString();

        if (phoneNumberStr.isEmpty() || !Patterns.PHONE.matcher(phoneNumberStr).matches()) {
            phoneNumberTextView.setError(getString(R.string.login_error_invalid_phone_number));
            valid = false;
        } else {
            phoneNumberTextView.setError(null);
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
}
