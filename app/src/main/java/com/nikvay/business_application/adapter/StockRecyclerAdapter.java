package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.ProductStock;

import java.util.ArrayList;


/**
 * Created by Param3 on 2/24/2016.
 */

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.MyDataHolder> {

    ArrayList<ProductStock> productStocks;
    Context context;

    public StockRecyclerAdapter(Context context, ArrayList<ProductStock> productStocks) {
        this.context = context;
        this.productStocks = productStocks;
    }

    @Override
    public int getItemCount() {
        return productStocks==null?0:productStocks.size();
    }

    @Override
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_stock, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final ProductStock productStock = productStocks.get(position);
        holder.txt_date.setText(productStock.getDate());
        holder.txt_quantity.setText(productStock.getQuantity() + " number");
        setScaleAnimation(holder.itemView);
    }

    public static class MyDataHolder extends RecyclerView.ViewHolder {

        TextView txt_date, txt_quantity;

        public MyDataHolder(View v) {
            super(v);
            txt_date = v.findViewById(R.id.txt_date);
            txt_quantity = v.findViewById(R.id.txt_quantity);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
