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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


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

        private TextView urgency, prescription, carNecessary;

        OrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            urgency = itemView.findViewById(R.id.textViewUrgency);
            prescription = itemView.findViewById(R.id.textViewPrescription);
            carNecessary = itemView.findViewById(R.id.textViewCarNecessary);
        }

        void setDetails(Order order) {
            StringBuffer urgencyText = new StringBuffer("Dringlichkeit: ")
                    .append(order.getUrgency());
            StringBuffer prescriptionText = new StringBuffer("Rezept: ")
                    .append((order.getPrescription().equals("yes") ? "ja" : "nein"));
            StringBuffer carNecessaryText = new StringBuffer("Auto erforderlich: " )
                    .append((order.getCarNecessary().equals("yes") ? "ja" : "nein"));

            urgency.setText(urgencyText.toString());
            prescription.setText(prescriptionText.toString());
            carNecessary.setText(carNecessaryText.toString());
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