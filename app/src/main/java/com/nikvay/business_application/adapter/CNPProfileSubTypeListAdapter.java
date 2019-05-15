package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.CNPProfileSubTypeListModel;
import com.nikvay.business_application.utils.SelectCNPProjectSubType;

import java.util.ArrayList;

public class CNPProfileSubTypeListAdapter extends RecyclerView.Adapter<CNPProfileSubTypeListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CNPProfileSubTypeListModel> cnpProfileSubTypeListModelArrayList;
    private boolean isDialog;
    private boolean isFirstLoad;
    private CNPProfileSubTypeListModel shareModel;
    private SelectCNPProjectSubType selectCNPProjectSubType;
    private boolean isNameSelect = false;

    public CNPProfileSubTypeListAdapter(Context mContext, ArrayList<CNPProfileSubTypeListModel> cnpProfileSubTypeListModelArrayList, boolean isDialog) {
        this.mContext=mContext;
        this.cnpProfileSubTypeListModelArrayList=cnpProfileSubTypeListModelArrayList;
        this.isFirstLoad = true;
        this.isDialog = isDialog;
        this.isNameSelect = false;
        if (isDialog) {
            this.shareModel = new CNPProfileSubTypeListModel();
        }
        this.selectCNPProjectSubType= (SelectCNPProjectSubType) mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_explodedview,parent,false);
        return  new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final MyViewHolder hold = holder;
        if (cnpProfileSubTypeListModelArrayList.get(position).isSubTypeSelected()) {
            hold.cardExploded.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            hold.cardExploded.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }

        hold.textExplodedPdf.setText(cnpProfileSubTypeListModelArrayList.get(position).getName());


        if (isDialog) {
            hold.cardExploded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isNameSelect) {
                        if (!cnpProfileSubTypeListModelArrayList.get(position).isSubTypeSelected()) {
                            for (int i = 0; i < cnpProfileSubTypeListModelArrayList.size(); i++) {
                                cnpProfileSubTypeListModelArrayList.get(i).setSubTypeSelected(false);
                            }
                            cnpProfileSubTypeListModelArrayList.get(position).setSubTypeSelected(true);
                            selectCNPProjectSubType.getProfileSubtype(cnpProfileSubTypeListModelArrayList.get(position).getId());
                            selectCNPProjectSubType.getSubPdfName(cnpProfileSubTypeListModelArrayList.get(position).getName());

                            shareModel = cnpProfileSubTypeListModelArrayList.get(position);
                        } else {
                            cnpProfileSubTypeListModelArrayList.get(position).setSubTypeSelected(false);
                        }
                        notifyDataSetChanged();
                    } else {
                        if (!cnpProfileSubTypeListModelArrayList.get(position).isSubTypeSelected()) {
                            for (int i = 0; i < cnpProfileSubTypeListModelArrayList.size(); i++) {
                                cnpProfileSubTypeListModelArrayList.get(i).setSubTypeSelected(false);
                            }
                            cnpProfileSubTypeListModelArrayList.get(position).setSubTypeSelected(true);
                            selectCNPProjectSubType.getProfileSubtype(cnpProfileSubTypeListModelArrayList.get(position).getId());
                            selectCNPProjectSubType.getSubPdfName(cnpProfileSubTypeListModelArrayList.get(position).getName());
                        } else {
                            selectCNPProjectSubType.getProfileSubtype(null);
                            selectCNPProjectSubType.getSubPdfName(null);

                            cnpProfileSubTypeListModelArrayList.get(position).setSubTypeSelected(false);
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {

        return cnpProfileSubTypeListModelArrayList==null?0:cnpProfileSubTypeListModelArrayList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView textExplodedPdf;
        CardView cardExploded;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textExplodedPdf=itemView.findViewById(R.id.textExplodedPdf);
            cardExploded=itemView.findViewById(R.id.cardExploded);
           // setScaleAnimation(itemView);
        }
    }
 /*   private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }*/
}
