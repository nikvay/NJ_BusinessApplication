package com.nikvay.business_application.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.SendPIActivity;
import com.nikvay.business_application.model.QuotationListModel;
import com.nikvay.business_application.utils.StaticContent;

import java.util.ArrayList;


/**
 * Created by Param3 on 2/24/2016.
 */

public class OrderProcessListAdapter extends RecyclerView.Adapter<OrderProcessListAdapter.MyDataHolder> implements Filterable {

    private ArrayList<QuotationListModel> arrayList;
    private ArrayList<QuotationListModel> arrayListFiltered;
    private Context context;

    public OrderProcessListAdapter(Context context, ArrayList<QuotationListModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListFiltered = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList==null?0:arrayList.size();
    }

    @Override
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_quotation_list, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final MyDataHolder hold = (MyDataHolder) holder;
        hold.textCountQL.setText("Quotation Count: " + String.valueOf(position + 1));
        hold.textQuotationNameQL.setText(arrayList.get(position).getQuote_num());
        hold.textCustomerNameQL.setText(arrayList.get(position).getCust_name());
        hold.textTotalAmountQL.setText(arrayList.get(position).getTot_amount());
        hold.textStatusQL.setText(arrayList.get(position).getStatus());
        if (arrayList.get(position).getStatus().equals(StaticContent.QuotationStatusCode.FOUR)) {
            hold.textStatusQL.setTextColor(context.getResources().getColor(R.color.green));
        } else if (arrayList.get(position).getStatus().equals(StaticContent.QuotationStatusCode.ZERO)) {
            hold.textStatusQL.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        } else if (arrayList.get(position).getStatus().equals(StaticContent.QuotationStatusCode.FIVE)) {
            hold.textStatusQL.setTextColor(context.getResources().getColor(R.color.yellow));
        } else {
            hold.textStatusQL.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        hold.cardQl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendPIActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(StaticContent.IntentType.QUOTATION_NUMBER, arrayList.get(position).getQuote_num());
                context.startActivity(intent);
            }
        });
        setScaleAnimation(holder.itemView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty() || charSequence.equals("")) {
                    arrayList = arrayListFiltered;
                } else {
                    ArrayList<QuotationListModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getQuote_num().toLowerCase().contains(charString.toLowerCase()) || arrayList.get(i).getCust_name().contains(charSequence)) {
                            filteredList.add(arrayList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        arrayList = filteredList;
                    } else {
                        arrayList = arrayListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList = (ArrayList<QuotationListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyDataHolder extends RecyclerView.ViewHolder {

        TextView textCountQL,
                textQuotationNameQL,
                textCustomerNameQL,
                textTotalAmountQL,
                textStatusQL;
        private CardView cardQl;

        public MyDataHolder(View v) {
            super(v);
            textCountQL =  v.findViewById(R.id.textCountQL);
            textQuotationNameQL =  v.findViewById(R.id.textQuotationNameQL);
            textCustomerNameQL =  v.findViewById(R.id.textCustomerNameQL);
            textTotalAmountQL =v.findViewById(R.id.textTotalAmountQL);
            textStatusQL =  v.findViewById(R.id.textStatusQL);
            cardQl = v.findViewById(R.id.cardQl);

        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
