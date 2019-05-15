package com.nikvay.business_application.adapter;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.Product;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * Created by Param3 on 2/24/2016.
 */

public class ProductPriceRecyclerAdapter extends RecyclerView.Adapter<ProductPriceRecyclerAdapter.MyDataHolder> implements Filterable {

    private ArrayList<Product> products;
    private ArrayList<Product> arrayListFiltered;
    private Context mContext;
    private OnItemClickListener listener;


    public ProductPriceRecyclerAdapter(Context mContext, ArrayList<Product> products) {
        this.mContext = mContext;
        this.products = products;
        this.arrayListFiltered=products;
    }

    @Override
    public int getItemCount() {
        return products==null?0:products.size();
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
    public MyDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_product_price, parent, false);
        MyDataHolder myDataHolder = new MyDataHolder(v);
        return myDataHolder;
    }

    @Override
    public void onBindViewHolder(final MyDataHolder holder, final int position) {
        final Product product = products.get(position);
        holder.txt_name.setText(product.getName());

        String price = product.getPrice();
        holder.txt_price.setText("Price " + price);
        /*holder.txt_discount.setText("Discount: " + product.getDiscount() + "%");

        double discount_price = 0;
        if (product.getPrice() != null){
            double discount = 0;
            if (product.getDiscount() != null){
                discount = Double.parseDouble(product.getDiscount());
            }
            double price = Double.parseDouble(product.getPrice());
            double discountPrice = price / 100 * discount;
            discount_price = price - discountPrice;
        }

        holder.txt_discounted_price.setText("Discount Price: " + discount_price);*/

        holder.iv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateOnClick.vibrate();

                listener.onAdapterClick(product, position);

//                Intent intent = new Intent(get, PriceDetailsActivity.class);
//                intent.putExtra("product", products.get(position));
//                mContext.startActivity(intent);
            }
        });

        setScaleAnimation(holder.itemView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s","").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    products = arrayListFiltered;
                } else {
                    ArrayList<Product> filteredList = new ArrayList<>();
                    for (int i = 0; i < products.size(); i++) {

                        String productName=products.get(i).getName().replaceAll("\\s","").toLowerCase().trim();
                        if (productName.contains(charString)) {
                            filteredList.add(products.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        products = filteredList;
                    } else {
                        products = arrayListFiltered;
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = products;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                products = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyDataHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_price; //, txt_discount, txt_discounted_price,
        ImageView iv_view_details;

        public MyDataHolder(View v) {
            super(v);
            VibrateOnClick.vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);

            txt_name = v.findViewById(R.id.txt_name);
            txt_price = v.findViewById(R.id.txt_price);
            //txt_discount = (TextView) v.findViewById(R.id.txt_discount);
            //txt_discounted_price = (TextView) v.findViewById(R.id.txt_discounted_price);
            iv_view_details = v.findViewById(R.id.iv_view_details);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }

    //=====================================================
    public interface OnItemClickListener {
        void onAdapterClick(Product productModule, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
