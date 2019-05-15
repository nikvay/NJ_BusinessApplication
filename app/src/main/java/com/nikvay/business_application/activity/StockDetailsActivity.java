package com.nikvay.business_application.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.StockRecyclerAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.CNP;
import com.nikvay.business_application.model.Product;
import com.nikvay.business_application.model.ProductStock;
import com.nikvay.business_application.utils.Logs;
import com.nikvay.business_application.utils.ResponseUtil;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.volley_support.MyVolleyPostFragmentMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tamboli on 18-Mar-18.
 */

public class StockDetailsActivity extends AppCompatActivity {

    private static final String TAG = StockDetailsActivity.class.getSimpleName();
    TextView txt_item_code, txt_stock, txt_quantity;
    ArrayList<ProductStock> productStocks;
    RecyclerView recyclerView;
    Product product;
    SharedUtil sharedUtil;
    private Button btnGenQuotation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Item Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        localBrodcastInitialize();
        initView();
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
            product = (Product) bundle.getSerializable("product");
            txt_item_code.setText(product.getName());
            callWebServices(product.getProduct_id());
        }
    }


    private void initView() {
        sharedUtil = new SharedUtil(StockDetailsActivity.this);
        productStocks = new ArrayList<ProductStock>();
        txt_item_code =  findViewById(R.id.txt_item_code);
        txt_stock = findViewById(R.id.txt_stock);
        txt_quantity = findViewById(R.id.txt_quantity);
        recyclerView = findViewById(R.id.recyclerView);
        btnGenQuotation = findViewById(R.id.btnGenQuotation);
        LinearLayoutManager linearlayout = new LinearLayoutManager(StockDetailsActivity.this);
        linearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearlayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(StockDetailsActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        events();
    }

    private void events() {
        btnGenQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StockDetailsActivity.this, RequestQuotationActivity.class));
            }
        });
    }

    private void callWebServices(String product_id) {
        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.STOCK_DETAILS:
                        try {
                            Logs.showLogE(TAG, response);
                            CNP cnp = ResponseUtil.getProductStock(response);
                            if (cnp.getError_code() == 1) {
                                if (cnp.getProductStocks().size() != 0) {
                                    productStocks.addAll(cnp.getProductStocks());
                                    txt_stock.setText("Stock as on " + cnp.getProductStocks().get(0).getDate());
                                    txt_quantity.setText(cnp.getProductStocks().get(0).getQuantity() + " number");
                                    productStocks.remove(0);
                                }
                                StockRecyclerAdapter adapter = new StockRecyclerAdapter(StockDetailsActivity.this, productStocks);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onTaskFailed(String response, int serviceCode) {
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.STOCK_DETAILS:
                        Logs.showLogE(TAG, response);
                        break;
                }
            }
        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.STOCK_DETAILS);
        map.put("user_id", sharedUtil.getUserDetails().getUser_id());
        map.put("product_id", product_id);
        new MyVolleyPostFragmentMethod(StockDetailsActivity.this, volleyCompleteListener, map, ServerConstants.ServiceCode.STOCK_DETAILS, true);
    }

}
