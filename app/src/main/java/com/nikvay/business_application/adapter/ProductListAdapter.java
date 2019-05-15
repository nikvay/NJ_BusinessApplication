package com.nikvay.business_application.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.model.ProductModel;
import com.nikvay.business_application.utils.SelectedProductInterface;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;

import java.util.ArrayList;


public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<ProductModel> arrayList;
    private ArrayList<ProductModel> arrayListFiltered;
    private boolean isDialog;
    private boolean isFirstLoad;
    private SelectedProductInterface selectedProductInterface;
    private SuccessDialog successDialog;


    public ProductListAdapter(Context mContext, ArrayList<ProductModel> arrayList, SelectedProductInterface selectedProductInterface, boolean isDialog) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.arrayListFiltered = arrayList;
        this.isFirstLoad = true;
        this.isDialog = isDialog;
        this.successDialog = new SuccessDialog(mContext);
        this.selectedProductInterface = (SelectedProductInterface) selectedProductInterface;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_product, parent, false);
        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Data hold = (Data) holder;

        if (isDialog) {
            if (arrayList.get(position).isSelected()) {
                hold.cardProductAdapter.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
            } else {
                hold.cardProductAdapter.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
            }
            hold.linearDiscount.setVisibility(View.GONE);

        } else {
            hold.linearDiscount.setVisibility(View.VISIBLE);
            hold.btnDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        arrayList.get(position).setStock_status(StaticContent.StockStatus.DELIVERY);
                        notifyDataSetChanged();
                    }

                }
            });
            hold.btnOkDesPLA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!hold.editDescriptionPLA.getText().toString().isEmpty()) {
                        hold.editDescriptionPLA.setError(null);
                        arrayList.get(position).setProduct_description(hold.editDescriptionPLA.getText().toString());
                        notifyDataSetChanged();
                        successDialog.showDialog("Description added", false);
                    } else {
                        hold.editDescriptionPLA.setError("Enter description");
                    }
                }
            });
            hold.btnExStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        arrayList.get(position).setStock_status(StaticContent.StockStatus.EX_STOCK);
                        notifyDataSetChanged();

                    }

                }
            });
/*
            if (arrayList.get(position).isApplied()) {
                hold.linearEnterDiscount.setVisibility(View.VISIBLE);
                hold.textEnetrDiscount.setText("Enter Discount for " + arrayList.get(position).getQuantity());
            } else {
                hold.linearEnterDiscount.setVisibility(View.GONE);
            }
*/
            hold.editQuantityPLA.setText(arrayList.get(position).getQuantity());
            hold.textEditDiscountPLA.setHint("0" + " to " + arrayList.get(position).getDiscount_limit());
            hold.btnApplyPLA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.valueOf(hold.textEditDiscountPLA.getText().toString()) <= 60) {
                        arrayList.get(position).setShowPriceAfterDiscount(String.valueOf(calculatePerShow(hold.textEditDiscountPLA.getText().toString(), arrayList.get(position).getQuantity(), arrayList.get(position).getPrice().replace(",", ""))));
                        arrayList.get(position).setPriceAfterDiscount(String.valueOf(calculatePer(hold.textEditDiscountPLA.getText().toString(), arrayList.get(position).getPrice().replace(",", ""))));
                        arrayList.get(position).setDiscountNumber(hold.textEditDiscountPLA.getText().toString());
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "Percentage should be between 0" + " to " + arrayList.get(position).getDiscount_limit(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            hold.textRemovePLA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.remove(arrayList.get(position));
                    selectedProductInterface.removeSelectedProduct();
                    notifyDataSetChanged();
                }
            });

            hold.btnQuantityPLA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hold.editQuantityPLA.getText().toString().isEmpty()) {
                        hold.editQuantityPLA.setText("1");
                    }

                    //  arrayList.get(position).setApplied(true);
                    if (Integer.valueOf(hold.editQuantityPLA.getText().toString()) > 0 && !hold.editQuantityPLA.getText().toString().isEmpty()) {
                        arrayList.get(position).setQuantity(hold.editQuantityPLA.getText().toString());
                        successDialog.showDialog("Quantity Added Successfully", false);
                        notifyDataSetChanged();
                    } else {
                        hold.editQuantityPLA.setError("Quantity should be greater then 0");
                        Toast.makeText(mContext, "Quantity should be greater then 0", Toast.LENGTH_SHORT).show();
                        hold.editQuantityPLA.setText("1");
                        notifyDataSetChanged();
                    }
                    selectedProductInterface.quantityNotify();

                }
            });
        }
        hold.textDiscountPLA.setText(arrayList.get(position).getShowPriceAfterDiscount());
        hold.textProductNamePLA.setText(arrayList.get(position).getName());
        hold.textCustomerNumberPLA.setText(String.valueOf(position + 1));
        hold.textPricePLA.setText(arrayList.get(position).getPrice());
        hold.textGrossPLA.setText(arrayList.get(position).getGross_wt());
        hold.textNetPLA.setText(arrayList.get(position).getNet_wt());
        hold.textStockPLA.setText(arrayList.get(position).getStock_count());
        hold.textDimensionPLA.setText(arrayList.get(position).getDimension());
        hold.texAccessoriesPLA.setText(arrayList.get(position).getAccessories());
        if (isDialog) {
            hold.cardProductAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!arrayList.get(position).isSelected()) {
                        arrayList.get(position).setSelected(true);
                        selectedProductInterface.addSelectedProduct(arrayList.get(position));
                    } else {
                        selectedProductInterface.subSelectedProduct(arrayList.get(position));
                        arrayList.get(position).setSelected(false);
                    }
                    notifyDataSetChanged();
                }
            });


        }
        if (isFirstLoad) {
            isFirstLoad = false;
            setScaleAnimation(hold.itemView);
        }

    }

    private float calculatePerShow(String disValue, String quantity, String price) {
        if (disValue.isEmpty()) {
            disValue = "0";
        }
        price = String.valueOf(Integer.valueOf(price) * Integer.valueOf(quantity));
        float mValue = Float.valueOf(disValue) / 100;
        float mPrice = Float.valueOf(price);
        float ans = mPrice - (mPrice * mValue);
        return ans;
    }

    private float calculatePer(String value, String price) {
        if (value.isEmpty()) {
            value = "0";
        }
        float mValue = Float.valueOf(value) / 100;
        float mPrice = Float.valueOf(price);
        float ans = mPrice - (mPrice * mValue);
        return ans;
    }


    @Override
    public int getItemCount() {
        return (arrayList == null) ? 0 : arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s","").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    arrayList = arrayListFiltered;
                } else {
                    ArrayList<ProductModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        String productName=arrayList.get(i).getName().replaceAll("\\s","").toLowerCase().trim();
                        if (productName.contains(charString)) {
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
                arrayList = (ArrayList<ProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Data extends RecyclerView.ViewHolder {
        private TextView textCustomerNumberPLA,
                textProductNamePLA,
                textPricePLA,
                textGrossPLA,
                textNetPLA,
                textDimensionPLA,
                textDiscountPLA,
                textRemovePLA,
                textStockPLA,
                textEnetrDiscount,
                texAccessoriesPLA;
        private Button btnApplyPLA,
                btnQuantityPLA,
                btnOkDesPLA;
        private EditText textEditDiscountPLA,
                editQuantityPLA,
                editDescriptionPLA;
        private CardView cardProductAdapter;
        private LinearLayout linearDiscount,
                linearEnterDiscount;
        private RadioButton btnExStock,
                btnDelivery;

        public Data(View itemView) {
            super(itemView);
            textCustomerNumberPLA =  itemView.findViewById(R.id.textCustomerNumberPLA);
            textProductNamePLA = itemView.findViewById(R.id.textProductNamePLA);
            textPricePLA =  itemView.findViewById(R.id.textPricePLA);
            textGrossPLA = itemView.findViewById(R.id.textGrossPLA);
            textNetPLA =  itemView.findViewById(R.id.textNetPLA);
            textStockPLA = itemView.findViewById(R.id.textStockPLA);
            textDiscountPLA = itemView.findViewById(R.id.textDiscountPLA);
            textDimensionPLA = itemView.findViewById(R.id.textDimensionPLA);
            cardProductAdapter = itemView.findViewById(R.id.cardProductAdapter);
            linearDiscount = itemView.findViewById(R.id.linearDiscount);
            linearEnterDiscount =itemView.findViewById(R.id.linearEnterDiscount);
            textEditDiscountPLA = itemView.findViewById(R.id.textEditDiscountPLA);
            editQuantityPLA = itemView.findViewById(R.id.editQuantityPLA);
            btnApplyPLA = itemView.findViewById(R.id.btnApplyPLA);
            btnQuantityPLA = itemView.findViewById(R.id.btnQuantityPLA);
            textRemovePLA = itemView.findViewById(R.id.textRemovePLA);
            textEnetrDiscount = itemView.findViewById(R.id.textEnetrDiscount);
            texAccessoriesPLA =  itemView.findViewById(R.id.texAccessoriesPLA);
            btnExStock = itemView.findViewById(R.id.btnExStock);
            btnDelivery = itemView.findViewById(R.id.btnDelivery);
            editDescriptionPLA = itemView.findViewById(R.id.editDescriptionPLA);
            btnOkDesPLA = itemView.findViewById(R.id.btnOkDesPLA);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
