package jetzt.machbarschaft.android.database.test;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import jetzt.machbarschaft.android.database.Authentication;

public class AuthenticationTest {
    private static AuthenticationTest test;

    private AuthenticationTest() {

    }



    public static AuthenticationTest getInstance() {
        if (test == null) {
            test = new AuthenticationTest();
        }
        return test;
    }

    public void runTests(Activity a) {
        getUser();
        Log.i("AuthenticationTest", "Tests start:");
        register(a);
        //login(a);
    }

    private void register(Activity a) {
        Authentication.getInstance().verifyNumber("+49 1577 9539312", a,smsCodeSent -> {
                    Log.i("AuthenticationTest", "SMS Code Sent:");
                    login(a);
                },
                verificationAndLoginSuccess -> {
            if(verificationAndLoginSuccess) {
                Log.i("AuthenticationTest", "register: Login Successful:");
                this.getUser();
            } else {
                Log.i("AuthenticationTest", "register: Login FAIL:");

            }
                });
    }
    private void login(Activity a) {
        Authentication.getInstance().verifyCode("123456", a, verificationAndLoginSuccess -> {
            if(verificationAndLoginSuccess) {
                Log.i("AuthenticationTest", "Login Successful:");
                this.getUser();
            } else {
                Log.i("AuthenticationTest", "Login FAIL:");

            }
        });
    }

    private void getUser() {
        FirebaseUser user = Authentication.getInstance().getCurrentUser();
        Log.i("AuthenticationTest", "user:" + user.toString());

    }

}
