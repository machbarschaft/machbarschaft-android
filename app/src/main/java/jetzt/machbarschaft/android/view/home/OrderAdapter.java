package jetzt.machbarschaft.android.view.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.OrderHandler;
import jetzt.machbarschaft.android.database.entitie.Order;


/**
 * Custom Adapter for the Recycler View to display the Orders.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Context context;
    private ArrayList<Order> orders;
    private Location location;
    private OrderClickListener listener;

    OrderAdapter(Context context, ArrayList<Order> orders, Location location, OrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.location = location;
        this.listener = listener;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_view_item, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order order = orders.get(position);
        double distance = 0;
        if (location != null) {
            distance = OrderHandler.getDistance(location.getLatitude(),
                    location.getLongitude(), order.getLatitude(), order.getLongitude()) / 1000;
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
        private TextView orderId;
        private TextView orderClientName;
        private TextView orderClientAddress;
        private ImageView orderIcon;
        private TextView orderDistance;

        OrderHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);

            orderId = itemView.findViewById(R.id.order_id);
            orderClientName = itemView.findViewById(R.id.order_client_name);
            orderClientAddress = itemView.findViewById(R.id.order_client_address);
            orderIcon = itemView.findViewById(R.id.order_icon);
            orderDistance = itemView.findViewById(R.id.order_distance);
        }

        void setDetails(Order order, double distance) {
            Context context = view.getContext();

            // Build extras
            String typeText = context.getString(order.getType().getTitle());
            List<String> extras = new ArrayList<>();
            if (order.getPrescription()) {
                extras.add(context.getString(R.string.home_order_extra_prescription));
            }
            if (order.isCarNecessary()) {
                extras.add(context.getString(R.string.home_order_extra_car));
            }
            String extrasText = joinStrings(", ", extras);

            final int urgencyColor = order.getUrgency().getColor(context);

            view.setTag(order.getId());
            orderId.setTextColor(urgencyColor);
            orderId.setText(String.valueOf(order.getListId()));
            orderClientAddress.setText(order.getShortAddress());
            orderClientName.setText(order.getClientName());
            orderIcon.setImageResource(order.getType().getIcon());
            orderIcon.setImageTintList(ColorStateList.valueOf(urgencyColor));
            orderDistance.setText(context.getString(R.string.home_order_distance_km, distance));
        }

        @Override
        public void onClick(View v) {
            final String orderId = (String) v.getTag();
            if (listener != null) {
                listener.onOrderClicked(orderId);
            }
        }
    }

    /**
     * Joins the strings from the list using the given delimiter.
     *
     * @param delimiter The delimiter is placed in between the elements.
     * @param strings   The elements to join to one string.
     * @return The joined elements in a single string.
     */
    private String joinStrings(String delimiter, List<String> strings) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(delimiter, strings);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strings.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(strings.get(i));
            }
            return sb.toString();
        }
    }

    public interface OrderClickListener {
        /**
         * Called when the specified order has been clicked.
         *
         * @param orderId The id of the order that has been clicked.
         */
        void onOrderClicked(String orderId);
    }
}