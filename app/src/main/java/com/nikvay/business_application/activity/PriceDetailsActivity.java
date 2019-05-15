package com.nikvay.business_application.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.Product;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;

/**
 * Created by Tamboli on 18-Mar-18.
 */

public class PriceDetailsActivity extends AppCompatActivity {

    private static final String TAG = PriceDetailsActivity.class.getSimpleName();
    TextView txt_item_code, txt_price, txt_discounted_price;
    EditText edt_discount;
    Button btn_calculate, btnGenQuotationD;
    Product product;
    SharedUtil sharedUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Item Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        localBrodcastInitialize();
        setIntentData();
    }

    private void localBrodcastInitialize() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(StaticContent.LocalBrodcastReceiverCode.CLOSE_ACTIVITY));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(StaticContent.LocalBrodcastReceiverCode.CLOSE_ACTIVITY);
            if (message.equals(StaticContent.LocalBrodcastReceiverCode.CLOSE_ACTIVITY)) {
                finish();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setIntentData() {
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null) {
            String name=bundle.getString("NAME");
            String price=bundle.getString("PRICE");
            txt_item_code.setText(name);
            txt_price.setText(price);

        }
       /* if (bundle != null) {
            product = (Product) bundle.getSerializable("product");
            txt_item_code.setText(product.getName());
            txt_price.setText(product.getPrice());
            *//*txt_discount.setText(product.getDiscount() + "%");
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
            txt_discounted_price.setText(discount_price + "");*//*
        }*/
    }

    private void initView() {
        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sharedUtil = new SharedUtil(PriceDetailsActivity.this);
        txt_item_code = findViewById(R.id.txt_item_code);
        txt_price =  findViewById(R.id.txt_price);
        edt_discount =  findViewById(R.id.edt_discount);
        txt_discounted_price =  findViewById(R.id.txt_discounted_price);
        btn_calculate = findViewById(R.id.btn_calculate);
        btnGenQuotationD =  findViewById(R.id.btnGenQuotationD);
        btnGenQuotationD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                startActivity(new Intent(PriceDetailsActivity.this, RequestQuotationActivity.class));
            }
        });
        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                try {
                    String discount1 = edt_discount.getText().toString();
                    String price1 = txt_price.getText().toString();
                    price1 = price1.replace(",", "");
                    double discount_price = 0;
                    if (price1 != null) {
                        double discount = 0;
                        if (discount1 != null) {
                            discount = Double.parseDouble(discount1);
                        }
                        double price = Double.parseDouble(price1);
                        double discountPrice = price / 100 * discount;
                        discount_price = price - discountPrice;
                    }
                    txt_discounted_price.setText(discount_price + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
