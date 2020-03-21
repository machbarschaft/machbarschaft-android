package com.ks.einanrufhilft.view.home;
package net.simplifiedlearning.recyclerviewexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.ks.einanrufhilft.R;
import com.ks.einanrufhilft.persistance.OrderDTO;

import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class OrderAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<OrderDTO> productList;

    //getting the context and product list with constructor
    public OrderAdapter(Context mCtx, List<OrderDTO> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products, null);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        OrderDTO product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText("text");
        holder.textViewShortDesc.setText("shortdsc");
        holder.textViewRating.setText(String.valueOf("rating"));
        holder.textViewPrice.setText(String.valueOf("price"));
        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}