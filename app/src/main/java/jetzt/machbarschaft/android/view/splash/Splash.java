package jetzt.machbarschaft.android.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.view.login.LoginMain;


/**
 * Splashscreen which will be displayed shortly when the App is started.
 */

public class Splash extends AppCompatActivity {
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myHandler.postDelayed(this::startLogin, 1200);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLogin() {
        this.startActivity(new Intent(this, LoginMain.class));
    }
}
