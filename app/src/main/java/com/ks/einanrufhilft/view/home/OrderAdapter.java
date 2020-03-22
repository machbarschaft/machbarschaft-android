package com.ks.einanrufhilft.view.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.R;

import java.util.ArrayList;


/**
 * Custom Adapter for the Recycler View to display the Orders.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context context;
    private ArrayList<Order> orders;

    OrderAdapter(Context context, ArrayList<Order> orders) {
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
        Order order = orders.get(position);
        holder.setDetails(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    /**
     * Sets the Order specific Text to each item and adds an On Click Listener
     */
    class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView orderType, einkaufsliste, distance;

        OrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            orderType = itemView.findViewById(R.id.textViewTitle);
            einkaufsliste = itemView.findViewById(R.id.textViewShortDesc);
            distance = itemView.findViewById(R.id.textViewDistance);
        }

        void setDetails(Order order) {
            //orderType.setText(order.getCategory());
            orderType.setText("Einkauf");
            einkaufsliste.setText(order.getPrescription());
            int zufallszahl = (int)(Math.random() * 200) + 1;
            distance.setText(String.format("%s Meter entfernt..", Integer.toString(zufallszahl)));
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