package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.ExplodedViewModel;
import com.nikvay.business_application.utils.SelectCNPProfileInterface;

import java.util.ArrayList;

public class CNPProfileAdapter extends RecyclerView.Adapter<CNPProfileAdapter.MyViewHolder>  {


    Context mContext;
    ArrayList<ExplodedViewModel> explodedViewModelsArrayKList;
    private boolean isDialog;
    private boolean isFirstLoad;
    private ExplodedViewModel shareModel;
    private SelectCNPProfileInterface selectCNPProfileInterface;
    private boolean isNameSelect = false;

    public CNPProfileAdapter(Context mContext, ArrayList<ExplodedViewModel> explodedViewModelsArrayKList,boolean isDialog) {
        this.mContext=mContext;
        this.explodedViewModelsArrayKList=explodedViewModelsArrayKList;
        this.isFirstLoad = true;
        this.isDialog = isDialog;
        this.isNameSelect = false;
        if (isDialog) {
            this.shareModel = new ExplodedViewModel();
        }
        this.selectCNPProfileInterface = (SelectCNPProfileInterface) mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_explodedview,parent,false);
        return  new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder hold, final int position) {
//        final MyViewHolder hold = holder;
        if (explodedViewModelsArrayKList.get(position).isSelected()) {
            hold.cardExploded.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            hold.cardExploded.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }


        hold.textExplodedPdf.setText(explodedViewModelsArrayKList.get(position).getPdfName());
        final String pdf_name = explodedViewModelsArrayKList.get(position).getName();

        if (pdf_name.equalsIgnoreCase("null")) {
            hold.iv_subtype.setVisibility(View.VISIBLE);
        }

        if (isDialog) {
            hold.cardExploded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isNameSelect) {
                        if (!explodedViewModelsArrayKList.get(position).isSelected()) {
                            for (int i = 0; i < explodedViewModelsArrayKList.size(); i++) {
                                explodedViewModelsArrayKList.get(i).setSelected(false);
                            }
                            explodedViewModelsArrayKList.get(position).setSelected(true);
                            selectCNPProfileInterface.getExplodedId(explodedViewModelsArrayKList.get(position).getId());
                            selectCNPProfileInterface.getPdfName(explodedViewModelsArrayKList.get(position).getPdfName());
                            selectCNPProfileInterface.getPdfType(pdf_name);

                            shareModel = explodedViewModelsArrayKList.get(position);
                        } else {
                            explodedViewModelsArrayKList.get(position).setSelected(false);
                        }
                        notifyDataSetChanged();
                    } else {
                        if (!explodedViewModelsArrayKList.get(position).isSelected()) {
                            for (int i = 0; i < explodedViewModelsArrayKList.size(); i++) {
                                explodedViewModelsArrayKList.get(i).setSelected(false);
                            }
                            explodedViewModelsArrayKList.get(position).setSelected(true);
                            selectCNPProfileInterface.getExplodedId(explodedViewModelsArrayKList.get(position).getId());
                            selectCNPProfileInterface.getPdfName(explodedViewModelsArrayKList.get(position).getPdfName());
                            selectCNPProfileInterface.getPdfType(pdf_name);


                        } else {
                            selectCNPProfileInterface.getExplodedId(null);

                            selectCNPProfileInterface.getPdfName(null);
                            selectCNPProfileInterface.getPdfType(null);

                            explodedViewModelsArrayKList.get(position).setSelected(false);
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return explodedViewModelsArrayKList==null?0:explodedViewModelsArrayKList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textExplodedPdf;
        ImageView iv_subtype;
        CardView cardExploded;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textExplodedPdf=itemView.findViewById(R.id.textExplodedPdf);
            cardExploded=itemView.findViewById(R.id.cardExploded);
            iv_subtype=itemView.findViewById(R.id.iv_subtype);
        }
    }
}
