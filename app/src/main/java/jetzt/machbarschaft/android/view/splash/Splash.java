package jetzt.machbarschaft.android.view.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.Authentication;
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
        fixGoogleMapBug();
        FirebaseUser user = Authentication.getInstance().getCurrentUser();
        if (user == null) {
            myHandler.postDelayed(this::startLogin, 1200);
        } else {
            myHandler.postDelayed(this::startApp, 1200);
        }

        // TESTS
        // DataAccessTest.getInstance().runTests(this);
        //TESTS
    }

    private void fixGoogleMapBug() {
        SharedPreferences googleBug = getSharedPreferences("google_bug", Context.MODE_PRIVATE);
        if (!googleBug.contains("fixed")) {
            File corruptedZoomTables = new File(getFilesDir(), "ZoomTables.data");
            corruptedZoomTables.delete();
            googleBug.edit().putBoolean("fixed", true).apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startApp() {
        this.startActivity(new Intent(this, Home.class));
    }

    private void startLogin() {
        this.startActivity(new Intent(this, LoginMain.class));
    }
}
