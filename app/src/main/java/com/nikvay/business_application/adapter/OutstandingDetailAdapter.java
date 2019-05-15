package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.OutstandingDetailModel;

import java.util.ArrayList;

public class OutstandingDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<OutstandingDetailModel> arrayList;
    private Context mContext;

    public OutstandingDetailAdapter(ArrayList<OutstandingDetailModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_outstanding_detail, parent, false);
        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Data hold = (Data) holder;
        hold.textReceiptNumberODA.setText("Ref.No. : "+arrayList.get(position).getRef_num());
        hold.textAmountODA.setText(arrayList.get(position).getAmount());
        hold.textReceiptDateODA.setText(arrayList.get(position).getDate());
        hold.textDueDateODA.setText(arrayList.get(position).getDue_date());
        hold.textOverDueDateODA.setText(arrayList.get(position).getOverdue_by_days());
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
        private TextView textReceiptNumberODA,
                textAmountODA,
                textReceiptDateODA,
                textDueDateODA,
                textOverDueDateODA;

        public Data(View itemView) {
            super(itemView);
            textReceiptNumberODA = itemView.findViewById(R.id.textReceiptNumberODA);
            textAmountODA = itemView.findViewById(R.id.textAmountODA);
            textReceiptDateODA = itemView.findViewById(R.id.textReceiptDateODA);
            textOverDueDateODA =  itemView.findViewById(R.id.textOverDueDateODA);
            textDueDateODA =  itemView.findViewById(R.id.textDueDateODA);

        }
    }
}
