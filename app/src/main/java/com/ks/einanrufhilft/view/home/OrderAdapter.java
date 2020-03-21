package com.ks.einanrufhilft.view.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.Database.OrderDTO;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context context;
    private ArrayList<OrderDTO> orders;

    OrderAdapter(Context context, ArrayList<OrderDTO> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        OrderDTO order = orders.get(position);
        holder.setDetails(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }



    class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        private TextView orderType, einkaufsliste, distance;

        OrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            orderType = itemView.findViewById(R.id.textViewTitle);
            einkaufsliste = itemView.findViewById(R.id.textViewShortDesc);
            distance = itemView.findViewById(R.id.textViewDistance);
        }

        void setDetails(OrderDTO order) {
            orderType.setText(order.getCategory());
            einkaufsliste.setText(order.getEinkaufszettel());
            distance.setText("200 Meter entfernt..");
            //txtDiameter.setText("test");
        }


        @Override
        public void onClick(View v) {
            Log.wtf("itemview", String.valueOf(getAdapterPosition()));
            Log.wtf("ID", String.valueOf(orders.get(getAdapterPosition()).getId()));
            Context context = v.getContext();
            //Intent intent = new Intent(context, JobActivty.class);
            //v.getContext().startActivity(intent);
        }
    }


}