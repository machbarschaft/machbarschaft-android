package com.ks.einanrufhilft.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.persistance.OrderDTO;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private List<OrderDTO> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initalizeData();

    }


    private void initalizeData(){
        orders = new ArrayList<>();
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
        orders.add(new OrderDTO());
    }
}
