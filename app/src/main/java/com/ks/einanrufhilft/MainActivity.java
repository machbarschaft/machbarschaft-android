package com.ks.einanrufhilft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.Database.Entitie.Account;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* TESTS */
        Database db = Database.getInstance();
        // String phone_number, String plz, String strasse, String hausnummer, String firstName, String lastNamme, String[] category) {
        //        this.phone_number = phone_number
       // Order o = new Order("0981238231", "12212", "myStarsse", "12a","alex",
         //       "maier", new String[]{"Einkauf"});

        //Account a = new Account("Max", "maier", "90821389123",7.5f, 30);
        //db.createAccount(a);

        db.getOrders();
    //db.setOrderStatus("mofVj419q6fAxj4hLYeW", Database.Status.Confirmed);


        /* TESTS */
    }
}
