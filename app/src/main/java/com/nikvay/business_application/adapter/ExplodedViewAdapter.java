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
import com.nikvay.business_application.utils.SelectExplodedView;

import java.util.ArrayList;

public class ExplodedViewAdapter extends RecyclerView.Adapter<ExplodedViewAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<ExplodedViewModel> explodedViewModelsArrayKList;
    private boolean isDialog;
    private boolean isFirstLoad;
    private ExplodedViewModel shareModel;
    private SelectExplodedView selectExplodedView;
    private boolean isNameSelect = false;

    public ExplodedViewAdapter(Context mContext, ArrayList<ExplodedViewModel> explodedViewModelsArrayKList,boolean isDialog) {
        this.mContext=mContext;
        this.explodedViewModelsArrayKList=explodedViewModelsArrayKList;
        this.isFirstLoad = true;
        this.isDialog = isDialog;
        this.isNameSelect = false;
        if (isDialog) {
            this.shareModel = new ExplodedViewModel();
        }
        this.selectExplodedView= (SelectExplodedView) mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_explodedview,parent,false);
        return  new  ExplodedViewAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final MyViewHolder hold = holder;
        if (explodedViewModelsArrayKList.get(position).isSelected()) {
            hold.cardExploded.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            hold.cardExploded.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }


        hold.textExplodedPdf.setText(explodedViewModelsArrayKList.get(position).getPdfName());



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
                                    selectExplodedView.getExplodedId(explodedViewModelsArrayKList.get(position).getId());
                                    selectExplodedView.getPdfName(explodedViewModelsArrayKList.get(position).getPdfName());
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
                                    selectExplodedView.getExplodedId(explodedViewModelsArrayKList.get(position).getId());
                                    selectExplodedView.getPdfName(explodedViewModelsArrayKList.get(position).getPdfName());
                                } else {
                                    selectExplodedView.getExplodedId(null);
                                    selectExplodedView.getPdfName(null);
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

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView textExplodedPdf;
        ImageView iv_subtype;
        CardView cardExploded;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textExplodedPdf=itemView.findViewById(R.id.textExplodedPdf);
            cardExploded=itemView.findViewById(R.id.cardExploded);
            //setScaleAnimation(itemView);
        }
    }
  /*  private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }*/
}
