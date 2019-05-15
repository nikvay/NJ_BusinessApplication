package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.VisitListModel;

import java.util.ArrayList;


/**
 * Created by Param3 on 2/24/2016.
 */

public class ViewVisitAdapter extends RecyclerView.Adapter<ViewVisitAdapter.MyDataHolder> {

    ArrayList<VisitListModel> arrayList;
    Context context;

    public ViewVisitAdapter(Context context, ArrayList<VisitListModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_visit_list, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final MyDataHolder hold = (MyDataHolder) holder;

        hold.textCountVA.setText(String.valueOf(position + 1));
        String customerName=arrayList.get(position).getCust_name();
        String contact_person=arrayList.get(position).getContact_person();
        String phone=arrayList.get(position).getTel();
        String email=arrayList.get(position).getEmail();
        String date=arrayList.get(position).getDate();
        String msg=arrayList.get(position).getMsg();
        if(customerName.equalsIgnoreCase(""))
        {
         hold.ll_customer.setVisibility(View.GONE);
        }
        if(contact_person.equalsIgnoreCase(""))
        {
            hold.ll_contact_person.setVisibility(View.GONE);
        }
        if(phone.equalsIgnoreCase(""))
        {
            hold.ll_phone.setVisibility(View.GONE);
        }
        if(email.equalsIgnoreCase(""))
        {
            hold.ll_email.setVisibility(View.GONE);
        }


        hold.textCustomerNameVA.setText(customerName);
        hold.textContactPerVA.setText(contact_person);
        hold.textPhoneNumberVA.setText(phone);
        hold.textEmailIDVA.setText(email);
        hold.textRegistrationDateVA.setText(date);
        hold.textMessageVA.setText(msg);
        setScaleAnimation(holder.itemView);
    }

    public static class MyDataHolder extends RecyclerView.ViewHolder {

        TextView textCountVA,
                textCustomerNameVA,
                textContactPerVA,
                textPhoneNumberVA,
                textEmailIDVA,
                textRegistrationDateVA,
                textMessageVA;


        LinearLayout ll_customer, ll_contact_person, ll_phone, ll_email, ll_date, ll_message;


        public MyDataHolder(View v) {
            super(v);
            textCountVA = v.findViewById(R.id.textCountVA);
            textCustomerNameVA = v.findViewById(R.id.textCustomerNameVA);
            textContactPerVA = v.findViewById(R.id.textContactPerVA);
            textPhoneNumberVA = v.findViewById(R.id.textPhoneNumberVA);
            textEmailIDVA = v.findViewById(R.id.textEmailIDVA);
            textRegistrationDateVA = v.findViewById(R.id.textRegistrationDateVA);
            textMessageVA = v.findViewById(R.id.textMessageVA);

            ll_customer = v.findViewById(R.id.ll_customer);
            ll_contact_person = v.findViewById(R.id.ll_contact_person);
            ll_phone = v.findViewById(R.id.ll_phone);
            ll_email = v.findViewById(R.id.ll_email);
            ll_date = v.findViewById(R.id.ll_date);
            ll_message = v.findViewById(R.id.ll_message);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
