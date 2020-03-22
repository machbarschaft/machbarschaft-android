package com.ks.einanrufhilft.view.home;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ks.einanrufhilft.Database.Entitie.Order;
import com.ks.einanrufhilft.Database.OrderHandler;
import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.view.order.OrderDetailActivity;

import java.util.ArrayList;



/**
 * Custom Adapter for the Recycler View to display the Orders.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private Location location;

    OrderAdapter(Context context, ArrayList<Order> orders, Location location) {
        this.context = context;
        this.orders = orders;
        this.location = location;
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
        double distance = 0;
        if (this.location != null) {
            distance = OrderHandler.getDistance(this.location.getLatitude(),
                    this.location.getLongitude(), order.getLat(), order.getLng()) / 1000;
        }
        holder.setDetails(order, distance);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    /**
     * Sets the Order specific Text to each item and adds an On Click Listener
     */
    class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private TextView urgency, prescription, carNecessary, distance;

        OrderHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            urgency = itemView.findViewById(R.id.textViewUrgency);
            prescription = itemView.findViewById(R.id.textViewPrescription);
            carNecessary = itemView.findViewById(R.id.textViewCarNecessary);
            distance = itemView.findViewById(R.id.textViewDistance);
        }

        void setDetails(Order order, double distance) {
            StringBuffer urgencyText = new StringBuffer("Dringlichkeit: ")
                    .append(order.getUrgency());
            StringBuffer prescriptionText = new StringBuffer("Rezept: ")
                    .append((order.getPrescription().equals("yes") ? "ja" : "nein"));
            StringBuffer carNecessaryText = new StringBuffer("Auto erforderlich: " )
                    .append((order.getCarNecessary().equals("yes") ? "ja" : "nein"));
            StringBuffer distanceText = new StringBuffer("Entfernung: ")
                    .append(Math.round(distance)).append(" km");


            this.view.setTag(order.getId());
            this.urgency.setText(urgencyText.toString());
            this.prescription.setText(prescriptionText.toString());
            this.carNecessary.setText(carNecessaryText.toString());
            this.distance.setText(distanceText.toString());
        }

        @Override
        public void onClick(View v) {
            Log.wtf("itemview", String.valueOf(getAdapterPosition()));
            Log.wtf("ID", String.valueOf(orders.get(getAdapterPosition()).getId()));
            Context context = v.getContext();
            //Intent intent = new Intent(context, JobActivty.class);
            //v.getContext().startActivity(intent);
            String orderId = (String) v.getTag();
            context.startActivity(new Intent(context, OrderDetailActivity.class)
                    .putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId));
        }
    }
}