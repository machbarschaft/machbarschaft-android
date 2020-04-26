package jetzt.machbarschaft.android.view.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.view.home.Home;
import jetzt.machbarschaft.android.view.login.LoginMain;

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
    private Button btnSendCode;
    private VerificationCallbacks verificationCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        verificationInProgress = false;
        verificationCallbacks = new VerificationCallbacks();

        // Get phone number
        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_PHONE_NUMBER)) {
            throw new IllegalArgumentException("Missing phone number extra!");
        }
        mPhoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);

        if (savedInstanceState != null) {
            verificationInProgress = savedInstanceState.getBoolean(SAVE_VERIFICATION_IN_PROGRESS, false);
            mPhoneNumber = savedInstanceState.getString(SAVE_PHONE_NUMBER, mPhoneNumber);
        }

        // Get UI elements
        Toolbar toolbar = findViewById(R.id.verify_phone_toolbar);
        Button btnSignIn = findViewById(R.id.button_sign_in);
        btnSendCode = findViewById(R.id.verify_phone_btn_send_code);
        EditText[] tfCodes = {
                findViewById(R.id.verificationTfCode1),
                findViewById(R.id.verificationTfCode2),
                findViewById(R.id.verificationTfCode3),
                findViewById(R.id.verificationTfCode4),
                findViewById(R.id.verificationTfCode5),
                findViewById(R.id.verificationTfCode6),
        };

        // Setup toolar
        toolbar.setNavigationOnClickListener(v -> navigateToLogin());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Button click handlers
        btnSendCode.setEnabled(false);
        btnSendCode.setOnClickListener(view -> startVerification());

        btnSignIn.setOnClickListener(view -> {
            // Combine user input of all text fields
            StringBuilder codeBuilder = new StringBuilder();
            for (EditText editText : tfCodes) {
                codeBuilder.append(editText.toString());
            }
            final String code = codeBuilder.toString();
            Log.d(LOG_TAG, "SMS code: " + code);

            onVerifySmsCode(code);
        });


        for (int i = 0; i < tfCodes.length; i++) {
            final int fieldNr = i;
            tfCodes[i].setOnKeyListener((v, keyCode, event) -> {
                // When the textfield is not the last and the user put something in this textfield then jump to the next field.
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode != KeyEvent.KEYCODE_DEL) {
                    if (fieldNr < tfCodes.length - 1 && tfCodes[fieldNr].getText().length() == 1) {
                        tfCodes[fieldNr + 1].requestFocus();
                    }
                }

                // When the textfield is empty, not the the first and the user press back in textfield then jump to the previous field.
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (fieldNr > 0 && tfCodes[fieldNr].getText().length() == 0 && keyCode == KeyEvent.KEYCODE_DEL) {
                        tfCodes[fieldNr - 1].requestFocus();
                    }
                }

                return false;
            });
        }
    }

    /**
     * Called when the user has entered the sms code and has clicked verify button.
     *
     * @param code The code that has been entered.
     */
    private void onVerifySmsCode(String code) {
        if (!verificationInProgress) {
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCallbacks.mVerificationId, code);
        signIn(credential);
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

        FirebaseAuth.getInstance().useAppLanguage();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mPhoneNumber, 90, TimeUnit.SECONDS, this, verificationCallbacks);
    }

    @Override
    public void onBackPressed() {
        navigateToLogin();
    }

    /**
     * Navigates back to the login activity.
     */
    private void navigateToLogin() {
        startActivity(new Intent(this, LoginMain.class)
                .putExtra(LoginMain.EXTRA_PHONE_NUMBER, mPhoneNumber));
        finishAfterTransition();
    }

    private void onLoginDone() {
        startActivity(new Intent(this, Home.class));
        finishAfterTransition();
    }

    /**
     * Call this method to sign into firebase.
     *
     * @param credential The credential to sign in with.
     */
    private void signIn(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onLoginDone();
                    } else {
                        Toast.makeText(this, R.string.verify_error_generic, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private class VerificationCallbacks extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        private PhoneAuthProvider.ForceResendingToken mResendToken;
        private String mVerificationId;

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signIn(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException exception) {
            Log.e(LOG_TAG, "Phone number verification failed", exception);

            int errorText = R.string.verify_error_generic;
            if (exception instanceof FirebaseTooManyRequestsException) {
                errorText = R.string.verify_error_sms_send_failed;
            }
            Toast.makeText(VerifyPhoneActivity.this, errorText, Toast.LENGTH_LONG).show();

            navigateToLogin();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            mVerificationId = verificationId;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String verificationId) {
            btnSendCode.setEnabled(true);
        }
    }
}