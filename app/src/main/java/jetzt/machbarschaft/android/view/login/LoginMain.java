package jetzt.machbarschaft.android.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

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
    public static final String EXTRA_PHONE_NUMBER = "phoneNumber";

    private EditText phoneNumberTextView;
    private Button loginButton;
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

        // Sets the Custom Pager Adapter to display the different slides in the application
        ViewPager introSlidesPager = findViewById(R.id.intro_slides_pager);
        TabLayout introSlidesIndicator = findViewById(R.id.intro_slides_indicator);

        introSlidesPager.setAdapter(new CustomPagerAdapter(this));
        introSlidesIndicator.setupWithViewPager(introSlidesPager, true);

        // Get UI elements
        phoneNumberTextView = findViewById(R.id.input_phone_number);
        loginButton = findViewById(R.id.btn_login);

        // Button click handlers
        loginButton.setOnClickListener(v -> login());

        String[] countryCodes = getResources().getStringArray(R.array.country_codes);
        ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, countryCodes);
        countryCodeTextView =
                findViewById(android.R.id.content)
                        .findViewById(R.id.filled_exposed_dropdown_country_Code_login);
        countryCodeTextView.setText(countryCodes[0], false);
        countryCodeTextView.setAdapter(countryCodeAdapter);

        // Fill in phone number if given
        final String phoneNumber = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
        if (phoneNumber != null) {
            for (String countryCode : countryCodes) {
                if (!phoneNumber.startsWith(countryCode)) {
                    continue;
                }

                final String phoneNumberSecondPart = phoneNumber.substring(countryCode.length());

                // Set data in views
                countryCodeTextView.setText(countryCode);
                phoneNumberTextView.setText(phoneNumberSecondPart);

                break;
            }
        }
    }

    public void login() {
        final String phoneNumber = PhoneNumberFormatterUtil.getPhoneNumber(
                countryCodeTextView.getText().toString(), phoneNumberTextView.getText().toString());

        if (!validate(phoneNumber)) {
            loginButton.setEnabled(true);
            return;
        }

        loginButton.setEnabled(false);

        startActivity(new Intent(this, VerifyPhoneActivity.class)
                .putExtra(VerifyPhoneActivity.EXTRA_PHONE_NUMBER, phoneNumber));
        finishAfterTransition();
    }

    /**
     * Checks if the Phone Number is actually valid.
     *
     * @param phoneNumber The phone number to validate.
     * @return True if phone number is legit.
     */
    public boolean validate(String phoneNumber) {
        boolean valid = false;


        if (phoneNumber != null && Patterns.PHONE.matcher(phoneNumber).matches()) {
            phoneNumberTextView.setError(null);
            valid = true;
        } else {
            phoneNumberTextView.setError(getString(R.string.login_error_invalid_phone_number));
        }

        return valid;
    }
}
