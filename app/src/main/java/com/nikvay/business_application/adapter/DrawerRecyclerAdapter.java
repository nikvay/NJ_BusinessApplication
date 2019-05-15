package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.Drawer;


import java.util.ArrayList;

/**
 * Created by callidus on 6/11/17.
 */

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.DrawerDataHolder> {

    ArrayList<Drawer> drawers;
    Context context;

    public DrawerRecyclerAdapter(Context context, ArrayList<Drawer> drawers) {
        this.context = context;
        this.drawers = drawers;
    }

    @Override
    public int getItemCount() {
        return drawers==null?0:drawers.size();
    }

    @Override
    public DrawerDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_drawer, parent, false);
        DrawerDataHolder drawerDataHolder = new DrawerDataHolder(v);
        return drawerDataHolder;
    }

    @Override
    public void onBindViewHolder(final DrawerDataHolder holder, final int position) {
        final Drawer drawer = drawers.get(position);
        final DrawerDataHolder hold = holder;
        holder.txt_name.setText(drawer.getCategory_name());
        holder.img_icon.setImageResource(drawer.getLogo());
    }

    public class DrawerDataHolder extends RecyclerView.ViewHolder {

        ImageView img_icon;
        TextView txt_name;

        public DrawerDataHolder(View v) {
            super(v);
            img_icon =  v.findViewById(R.id.img_icon);
            txt_name =v.findViewById(R.id.txt_name);
        }
    }
}

