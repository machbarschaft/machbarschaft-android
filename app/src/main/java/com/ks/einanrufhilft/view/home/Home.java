package com.ks.einanrufhilft.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ks.einanrufhilft.Database.Database;
import com.ks.einanrufhilft.Database.Entitie.Account;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.Database.OrderDTO;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private List<OrderDTO> orders;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        /* TESTS */
        Database db = Database.getInstance();
        // String phone_number, String plz, String strasse, String hausnummer, String firstName, String lastNamme, String[] category) {
        //        this.phone_number = phone_number
        // Order o = new Order("0981238231", "12212", "myStarsse", "12a","alex",
        //       "maier", new String[]{"Einkauf"});

        Account a = new Account("Max", "maier", "90821389123",7.5f, 30);

        db.getOrders();
        db.setOrderConfirmed("mofVj419q6fAxj4hLYeW", Database.Status.Confirmed);

        //db.createAccount(a);

        /* TESTS */

        initalizeData();
        initView();

    }


    private void initalizeData(){
        orders = new ArrayList<>();
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
    }

    public void initView(){
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(this, (ArrayList<OrderDTO>) orders);
        recyclerView.setAdapter(orderAdapter);
    }
}
