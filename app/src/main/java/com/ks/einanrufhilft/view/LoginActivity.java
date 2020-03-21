package com.ks.einanrufhilft.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ks.einanrufhilft.R;

public class LoginActivity extends AppCompatActivity {

    private static final int HANDLER_SIGNUP = 0;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupHandle = findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Signup
            }
        });
    }


    public void login() {
        if (validate()) {
            loginButton.setEnabled(false);

            Context context;
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Anmeldung...");
            progressDialog.show();

            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

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

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }

    private void onLoginSuccess(){
        //TODO
    }

    private void onLoginFailed(){
        //TODO
    }
}
