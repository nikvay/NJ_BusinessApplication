package com.nikvay.business_application.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.OutstandingDetailActivity;
import com.nikvay.business_application.model.OutstandingModel;
import com.nikvay.business_application.utils.StaticContent;

import java.util.ArrayList;

public class OutstandingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<OutstandingModel> arrayList;
    private Context mContext;

    public OutstandingAdapter(ArrayList<OutstandingModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_outstanding, parent, false);
        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Data hold = (Data) holder;
        hold.textCustomerNumberOA.setText("Customer Number: " + String.valueOf(position + 1));
        hold.textCustomerNameOA.setText(arrayList.get(position).getCompany_name());
        hold.textEmailIOA.setText(arrayList.get(position).getEmail_id());
        hold.textOutstandingOA.setText(arrayList.get(position).getAmount());
        hold.cardOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OutstandingDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(StaticContent.IntentKey.CUSTOMER_ID, arrayList.get(position).getC_id());
                intent.putExtra(StaticContent.IntentKey.TOTAL_OUTSTANDING, arrayList.get(position).getAmount());
                mContext.startActivity(intent);
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
        private TextView textCustomerNumberOA,
                textCustomerNameOA,
                textEmailIOA,
                textOutstandingOA;
        private CardView cardOA;

        public Data(View itemView) {
            super(itemView);
            textCustomerNumberOA =  itemView.findViewById(R.id.textCustomerNumberOA);
            textCustomerNameOA = itemView.findViewById(R.id.textCustomerNameOA);
            textEmailIOA =  itemView.findViewById(R.id.textEmailIOA);
            textOutstandingOA = itemView.findViewById(R.id.textOutstandingOA);
            cardOA = itemView.findViewById(R.id.cardOA);

        }
    }
}
