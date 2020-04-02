package jetzt.machbarschaft.android.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.view.home.Home;
import jetzt.machbarschaft.android.view.login.LoginMain;


/**
 * Splashscreen which will be displayed shortly when the App is started.
 */
public class Splash extends AppCompatActivity {
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Storage.getInstance().getUserID() == null) {
            myHandler.postDelayed(this::startLogin, 1200);
        } else {
            myHandler.postDelayed(this::startApp, 1200);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startApp() {
        startActivity(new Intent(this, Home.class));
        finishAfterTransition();
    }

    private void startLogin() {
        startActivity(new Intent(this, LoginMain.class));
        finishAfterTransition();
    }
}
