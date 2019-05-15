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
import com.nikvay.business_application.model.ViewQuotationModel;
import com.nikvay.business_application.utils.MathCalculation;

import java.util.ArrayList;


/**
 * Created by Param3 on 2/24/2016.
 */

public class QuotationListProductAdapter extends RecyclerView.Adapter<QuotationListProductAdapter.MyDataHolder> {

    ArrayList<ViewQuotationModel> arrayList;
    Context context;
    private MathCalculation mathCalculation;

    public QuotationListProductAdapter(Context context, ArrayList<ViewQuotationModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.mathCalculation = new MathCalculation();
    }

    @Override
    public int getItemCount() {
        return arrayList==null?0:arrayList.size();
    }

    @Override
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_view_quotation_product, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final MyDataHolder hold = (MyDataHolder) holder;


        hold.textProductNumberVQP.setText(String.valueOf(position + 1));
        hold.textProductNameVQP.setText(arrayList.get(position).getName());
        hold.textPriceVQP.setText(arrayList.get(position).getPrice());
        hold.textGrossVQP.setText(arrayList.get(position).getGross_wt());
        hold.textDimensionVQP.setText(arrayList.get(position).getDimension());
        hold.textQuantityVQP.setText(arrayList.get(position).getProduct_qty());
        hold.textDisValueVQP.setText("Discount: " + arrayList.get(position).getDiscount_value() + " % ");
        hold.textFromuVQP.setText(arrayList.get(position).getmQbasedPrice() + " - " + arrayList.get(position).getDiscount_value() + " % ");
        hold.textGrandPVPQ.setText("Grand Price : " + mathCalculation.calculatePer(arrayList.get(position).getDiscount_value(), arrayList.get(position).getmQbasedPrice()));
        hold.textNetVQP.setText(arrayList.get(position).getNet_wt());
        setScaleAnimation(holder.itemView);
    }

    /*private float calculatePer(String value, String price) {
        if (value.isEmpty()) {
            value = "0";
        }
        float mValue = Float.valueOf(value) / 100;
        float mPrice = Float.valueOf(price);
        float ans = mPrice - (mPrice * mValue);
        return ans;
    }*/

    public static class MyDataHolder extends RecyclerView.ViewHolder {

        TextView textProductNumberVQP,
                textProductNameVQP,
                textPriceVQP,
                textGrossVQP,
                textDimensionVQP,
                textDisValueVQP,
                textFromuVQP,
                textGrandPVPQ,
                textNetVQP,
                textQuantityVQP;

        public MyDataHolder(View v) {
            super(v);
            textProductNumberVQP = v.findViewById(R.id.textProductNumberVQP);
            textProductNameVQP =  v.findViewById(R.id.textProductNameVQP);
            textPriceVQP = v.findViewById(R.id.textPriceVQP);
            textGrossVQP = v.findViewById(R.id.textGrossVQP);
            textDimensionVQP =  v.findViewById(R.id.textDimensionVQP);
            textDisValueVQP = v.findViewById(R.id.textDisValueVQP);
            textFromuVQP = v.findViewById(R.id.textFromuVQP);
            textGrandPVPQ =  v.findViewById(R.id.textGrandPVPQ);
            textNetVQP =  v.findViewById(R.id.textNetVQP);
            textQuantityVQP =  v.findViewById(R.id.textQuantityVQP);

        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
