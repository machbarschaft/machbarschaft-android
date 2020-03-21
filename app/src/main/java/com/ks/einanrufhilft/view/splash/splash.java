package com.ks.einanrufhilft.view.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.LoginMain;

public class splash extends AppCompatActivity {

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
        }, 900);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLogin() {
        this.startActivity(new Intent(this, LoginMain.class));
    }
}
