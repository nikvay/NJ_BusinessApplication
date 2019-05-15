package com.nikvay.business_application.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.NotificationDetailsActivity;
import com.nikvay.business_application.model.NotificationModule;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context mContext;
    private ArrayList<NotificationModule> notificationModuleArrayList;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModule> notificationModuleArrayList) {
        this.mContext = mContext;
        this.notificationModuleArrayList = notificationModuleArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final NotificationModule notificationModule = notificationModuleArrayList.get(position);

        final String name=notificationModule.getTitle();
        final String subName=notificationModule.getPdf_name();
        final String date=notificationModule.getDate();
        final String file_path=notificationModule.getImage_path();

        holder.tv_notification_name.setText(name);
        holder.tv_notification_title.setText(subName);
        holder.tv_notification_date.setText(date);


        holder.cardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, NotificationDetailsActivity.class);
                intent.putExtra("TITLE",name);
                intent.putExtra("FILENAME",subName);
                intent.putExtra("DATE",date);
                intent.putExtra("FILEPATH",file_path);
                 mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationModuleArrayList==null?0:notificationModuleArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notification_name,tv_notification_title,tv_notification_date;
        ImageView iv_notification_download;
        CardView cardNotification;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notification_name = itemView.findViewById(R.id.tv_notification_name);
            tv_notification_title = itemView.findViewById(R.id.tv_notification_title);
            tv_notification_date = itemView.findViewById(R.id.tv_notification_date);
            iv_notification_download = itemView.findViewById(R.id.iv_notification_download);
            cardNotification=itemView.findViewById(R.id.cardNotification);
        }
    }
}
