package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.ViewQuotationModel;
import com.nikvay.business_application.utils.MathCalculation;
import com.nikvay.business_application.utils.QuotationUpdateNotifier;

import java.util.ArrayList;


/**
 * Created by Param3 on 2/24/2016.
 */

public class QuotationEditAdapter extends RecyclerView.Adapter<QuotationEditAdapter.MyDataHolder> {

    ArrayList<ViewQuotationModel> arrayList;
    Context context;
    private MathCalculation mathCalculation;
    private QuotationUpdateNotifier quotationUpdateNotifier;

    public QuotationEditAdapter(Context context, ArrayList<ViewQuotationModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.quotationUpdateNotifier = (QuotationUpdateNotifier) context;
        this.mathCalculation = new MathCalculation();
    }

    @Override
    public int getItemCount() {
        return arrayList==null?0:arrayList.size();
    }

    @Override
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_edit_quotation, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final MyDataHolder hold = (MyDataHolder) holder;
        hold.textProductNumberEQ.setText(String.valueOf(position + 1));
        hold.textProductNameEQ.setText(arrayList.get(position).getName());
        hold.textPriceEQ.setText(arrayList.get(position).getPrice());
        hold.textGrossEQ.setText(arrayList.get(position).getGross_wt());
        hold.textDimensionEQ.setText(arrayList.get(position).getDimension());
        hold.textQuantityEQ.setText(arrayList.get(position).getProduct_qty());
        hold.textDisValueEQ.setText("Discount: " + arrayList.get(position).getDiscount_value() + " % ");
        hold.textFromuEQ.setText(arrayList.get(position).getmQbasedPrice() + " - " + arrayList.get(position).getDiscount_value() + " % ");
        hold.textGrandEQ.setText("Grand Price : " + String.format("%.0f", mathCalculation.calculatePer(arrayList.get(position).getDiscount_value(), arrayList.get(position).getmQbasedPrice())));
        hold.textNetEQ.setText(arrayList.get(position).getNet_wt());
        hold.editQuantityEQ.setText(arrayList.get(position).getProduct_qty());
        hold.btnQuantityEQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hold.editQuantityEQ.getText().toString().isEmpty()){
                    hold.editQuantityEQ.setError("Enter quantity");
                    return;
                }
                if (Integer.valueOf(hold.editQuantityEQ.getText().toString()) <= 0) {
                    hold.editQuantityEQ.setError("Quantity cannot be zero");
                } else {
                    hold.editQuantityEQ.setError(null);
                    quotationUpdateNotifier.updateQuotation(position, hold.editQuantityEQ.getText().toString());
                }
            }
        });

        hold.textRemoveEQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quotationUpdateNotifier.deleteQuotation(position);
            }
        });
    }


    public static class MyDataHolder extends RecyclerView.ViewHolder {

        TextView textProductNumberEQ,
                textProductNameEQ,
                textPriceEQ,
                textGrossEQ,
                textNetEQ,
                textDimensionEQ,
                textQuantityEQ,
                textDisValueEQ,
                textFromuEQ,
                textRemoveEQ,
                textGrandEQ;
        private EditText editQuantityEQ;
        private Button btnQuantityEQ;

        public MyDataHolder(View v) {
            super(v);
            textProductNumberEQ = v.findViewById(R.id.textProductNumberEQ);
            textProductNameEQ = v.findViewById(R.id.textProductNameEQ);
            textPriceEQ = v.findViewById(R.id.textPriceEQ);
            textGrossEQ = v.findViewById(R.id.textGrossEQ);
            textNetEQ =  v.findViewById(R.id.textNetEQ);
            textDimensionEQ = v.findViewById(R.id.textDimensionEQ);
            textQuantityEQ =  v.findViewById(R.id.textQuantityEQ);
            textDisValueEQ = v.findViewById(R.id.textDisValueEQ);
            textFromuEQ =  v.findViewById(R.id.textFromuEQ);
            textRemoveEQ =  v.findViewById(R.id.textRemoveEQ);
            textGrandEQ = v.findViewById(R.id.textGrandEQ);
            editQuantityEQ = v.findViewById(R.id.editQuantityEQ);
            btnQuantityEQ =  v.findViewById(R.id.btnQuantityEQ);

        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
