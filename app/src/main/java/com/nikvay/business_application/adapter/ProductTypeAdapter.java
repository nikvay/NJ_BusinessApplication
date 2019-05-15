package com.nikvay.business_application.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ProductTypeNotifier;
import com.nikvay.business_application.model.ProductTypeModel;

import java.util.ArrayList;

public class ProductTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ProductTypeModel> arrayList;
    private Context mContext;
    private ProductTypeNotifier productTypeNotifier;
    int row_index;

    public ProductTypeAdapter(ArrayList<ProductTypeModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.productTypeNotifier = (ProductTypeNotifier) mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_product_type, parent, false);
        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final Data hold = (Data) holder;
        hold.textNamePTA.setText(arrayList.get(position).getType_name());
        hold.cardPTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold.cardPTA.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                //hold.cardProductAdapter.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productTypeNotifier.selectedProductType(arrayList.get(position).getId());
                    }
                },1000);

            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList==null?0:arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Data extends RecyclerView.ViewHolder {
        private TextView textNamePTA;
        private CardView cardPTA;

        public Data(View itemView) {
            super(itemView);
            textNamePTA =itemView.findViewById(R.id.textNamePTA);
            cardPTA =  itemView.findViewById(R.id.cardPTA);


        }
    }
}
