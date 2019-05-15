package com.nikvay.business_application.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.StockDetailsActivity;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.Product;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * Created by Param3 on 2/24/2016.
 */

public class ProductStockRecyclerAdapter extends RecyclerView.Adapter<ProductStockRecyclerAdapter.MyDataHolder> {

    private ArrayList<Product> products;
    private Context context;

    public ProductStockRecyclerAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getItemCount() {
        return products==null?0:products.size();
    }

    @Override
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_product_stock, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final Product product = products.get(position);
        holder.txt_name.setText(product.getName());
        holder.txt_quantity.setText("Stock: " + product.getStock());

        holder.iv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StockDetailsActivity.class);
                intent.putExtra("product", products.get(position));
                context.startActivity(intent);
            }
        });
        setScaleAnimation(holder.itemView);
    }

    public class MyDataHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_quantity, txt_view_details;
        ImageView iv_view_details;

        public MyDataHolder(View v) {
            super(v);
            VibrateOnClick.vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            txt_name = v.findViewById(R.id.txt_name);
            txt_quantity =  v.findViewById(R.id.txt_quantity);
//            txt_view_details = v.findViewById(R.id.txt_view_details);
            iv_view_details = v.findViewById(R.id.iv_view_details);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
