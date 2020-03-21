package com.ks.einanrufhilft.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

        initalizeData();
        initView();

    }


    private void initalizeData() {
        orders = new ArrayList<>();
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
    }

    public void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(this, (ArrayList<OrderDTO>) orders);
        recyclerView.setAdapter(orderAdapter);
    }
}
