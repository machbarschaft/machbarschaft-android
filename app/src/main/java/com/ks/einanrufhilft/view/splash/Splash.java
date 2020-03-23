package com.ks.einanrufhilft.view.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.LoginMain;


/**
 *  Splashscreen which will be displayed shortly when the App is started.
 */

public class Splash extends AppCompatActivity {

    private Handler myHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startLogin();
            }
        }, 1200);

        Database.getInstance().getMyOrder("0JTWfaKt6faqpqWtOHsR");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLogin() {
        this.startActivity(new Intent(this, LoginMain.class));
    }
}
