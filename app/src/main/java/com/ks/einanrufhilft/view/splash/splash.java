package com.ks.einanrufhilft.view.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.login.LoginMain;


/**
 *  Splashscreen which will be displayed shortly when the App is started.
 */
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
        }, 1200);



        /* TESTS */
        /*
        Database db = Database.getInstance();
        // String phone_number, String plz, String strasse, String hausnummer, String firstName, String lastNamme, String[] category) {
        //        this.phone_number = phone_number
        // Order o = new Order("0981238231", "12212", "myStarsse", "12a","alex",
        db.login("12345");


        db.getOrders();
        try {
            db.setOrderStatus("4HjAuNrmEccaterBBWTO", Database.Status.Closed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //db.setOrderStatus("mofVj419q6fAxj4hLYeW", Database.Status.Confirmed);

*/
        /* TESTS */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startLogin() {
        this.startActivity(new Intent(this, LoginMain.class));
    }
}
