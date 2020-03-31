package jetzt.machbarschaft.android.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * Displays the Login Page. Handles the Input and the Login Success / Login Failed.
 * Also can redirect to the register Page.
 */
public class LoginMain extends AppCompatActivity {
    private EditText phoneNumber;
    private Button loginButton;
    private Context context;
    private ProgressDialog progressDialog;
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


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_in_progress));
        progressDialog.show();

        loginButton.setOnClickListener(v -> login());
    }

    // TODO Check if phone number is registered
    public void login() {
        if (validate()) {
            loginButton.setEnabled(false);
            progressDialog.show();
            String phoneNumberStr = phoneNumber.getText().toString();

            DataAccess.getInstance().existUser(phoneNumberStr,exist -> {
                if (exist) {

                }
                else
                {
                    Toast.makeText(this, R.string.login_error_number_not_exist, Toast.LENGTH_SHORT).show();
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

    private void onErrorWhileLoading()
    {
        loginButton.setEnabled(true);
        progressDialog.dismiss();
    }

    /**
     * Handles the case when the Login didn't work.
     * Right now we show a small Toast which will show that the Login wasn't successfully.
     */
    private void onLoginFailed() {
        Toast.makeText(this, R.string.login_error_generic, Toast.LENGTH_SHORT).show();
    }
}
