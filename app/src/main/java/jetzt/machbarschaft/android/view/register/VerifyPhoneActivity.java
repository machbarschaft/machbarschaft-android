package jetzt.machbarschaft.android.view.register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.common.collect.Lists;
import java.util.List;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Authentication;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.entitie.Account;
import jetzt.machbarschaft.android.view.home.Home;
import jetzt.machbarschaft.android.view.register.sms.SMSData;
import jetzt.machbarschaft.android.view.register.sms.SMSEventListener;
import jetzt.machbarschaft.android.view.register.sms.SMSEventListenerImpl;

/**
 * For verifying via SMS.
 */
public class VerifyPhoneActivity extends AppCompatActivity {
    private static final String LOG_TAG = "VerifyPhoneActivity";
    public static final String EXTRA_PHONE_NUMBER = "phoneNumber";
    private static final String SAVE_PHONE_NUMBER = "phoneNumber";
    private static final String SAVE_VERIFICATION_IN_PROGRESS = "verificationInProgress";


    private String mPhoneNumber;
    private boolean verificationInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        // Get phone number
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PHONE_NUMBER)) {
            mPhoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
        } else {
            Log.e(LOG_TAG, "Missing phone number!");
        }

        if (savedInstanceState != null) {
            verificationInProgress = savedInstanceState.getBoolean(SAVE_VERIFICATION_IN_PROGRESS, false);
            mPhoneNumber = savedInstanceState.getString(SAVE_PHONE_NUMBER, mPhoneNumber);
        }

        // Get user data from calling activity
        Intent caller = getIntent();
        // Data for registration in array order: phone code, name, surname, address, phone number

        // Get UI elements
        Toolbar toolbar = findViewById(R.id.verify_phone_toolbar);
        Button btnSignIn = findViewById(R.id.button_sign_in);
        Button btnSendCode = findViewById(R.id.verify_phone_btn_send_code);
        EditText tfCode1 = findViewById(R.id.verificationTfCode1);
        EditText tfCode2 = findViewById(R.id.verificationTfCode2);
        EditText tfCode3 = findViewById(R.id.verificationTfCode3);
        EditText tfCode4 = findViewById(R.id.verificationTfCode4);
        EditText tfCode5 = findViewById(R.id.verificationTfCode5);
        EditText tfCode6 = findViewById(R.id.verificationTfCode6);


        List<EditText> tfCodes = Lists.newArrayList(tfCode1,tfCode2,tfCode3,tfCode4,tfCode5,tfCode6);

                // Setup toolar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // Use SMS to verify phone number
        SMSData smsData = (SMSData) caller.getSerializableExtra("smsData");
        Account account = (Account) caller.getSerializableExtra("account");
        SMSEventListener smsEventListener = new SMSEventListenerImpl() {
            @Override
            public void onSucceedLogin(Activity activity, String userID) {
                if (account != null) {
                    account.setId(userID);
                    DataAccess.getInstance().createAccount(account, successful -> {
                        super.onSucceedLogin(activity, userID);
                    });
                } else {
                    super.onSucceedLogin(activity, userID);
                }
            }
        };

        // Button click handlers
        btnSendCode.setOnClickListener(view -> {
            startVerification();
        });

        btnSignIn.setOnClickListener(view -> {
            // Combine user input of all textfields
            String code = TextUtils.join("", Lists.transform(tfCodes, EditText::getText));
            Log.d(LOG_TAG, "SMS code: " + code);
            Authentication.getInstance().verifyCode(code, this, successful -> {
                if (successful) {
                    Log.i(LOG_TAG, "SMS code verified");
                    onLoginDone();
                } else {
                    // TODO login failed
                    Log.e(LOG_TAG, "SMS code could not be verified");
                }
            });
        });


        for (int i = 0; i < tfCodes.size(); i++) {
            int j = i;
            tfCodes.get(i).setOnKeyListener((v, keyCode, event) -> {
                if(
                    j<tfCodes.size()-1 &&
                    tfCodes.get(j).getText().length() == 1
                )
                {
                    tfCodes.get(j+1).requestFocus();
                }
                return false;
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startVerification();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_PHONE_NUMBER, mPhoneNumber);
        outState.putBoolean(SAVE_VERIFICATION_IN_PROGRESS, verificationInProgress);
    }

    private void startVerification() {
        verificationInProgress = true;
        Authentication.getInstance().verifyNumber(mPhoneNumber, this, successful -> {
            if (successful) {
                Log.i(LOG_TAG, "Sent verification sms");
                // TODO do something
            } else {
                Log.e(LOG_TAG, "Failed to send verification sms");
                // TODO failed to send sms
            }
        }, successful -> {
            if (successful) {
                // Login done
                Log.i(LOG_TAG, "Verification successful");
                onLoginDone();
            } else {
                Log.e(LOG_TAG, "Phone number verification failed");
                // TODO verification failed
            }
        });
    }

    private void onLoginDone() {
        startActivity(new Intent(this, Home.class));
        finishAfterTransition();
    }
}