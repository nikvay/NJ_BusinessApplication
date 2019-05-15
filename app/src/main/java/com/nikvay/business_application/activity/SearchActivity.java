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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.ProductPriceRecyclerAdapter;
import com.nikvay.business_application.adapter.ProductStockRecyclerAdapter;
import com.nikvay.business_application.common.RecyclerItemClickListener;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.CNP;
import com.nikvay.business_application.model.Product;
import com.nikvay.business_application.utils.Logs;
import com.nikvay.business_application.utils.ResponseUtil;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by callidus on 28/11/17.
 */

public class SearchActivity extends AppCompatActivity implements VolleyCompleteListener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    EditText edt_search;
    TextView txt_no_data_found;
    RecyclerView recyclerView;
    ProductPriceRecyclerAdapter adapter1;
    ProductStockRecyclerAdapter adapter2;
    ArrayList<Product> products;
    SharedUtil sharedUtil;
    String type = "ZZ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
    private void setIntentData() {
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null) {
            type = (String) bundle.getString("type");
            //incomeExpenses = (IncomeExpenses) bundle.getSerializable("income_expenses");
        }
    }

    private void initView() {
        sharedUtil = new SharedUtil(SearchActivity.this);
        products = new ArrayList<Product>();
        edt_search = findViewById(R.id.edt_search);
        txt_no_data_found = findViewById(R.id.txt_no_data_found);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearlayout = new LinearLayoutManager(SearchActivity.this);
        linearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearlayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                    }
                })
        );

        edt_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                            if (!text.isEmpty()) {
                                if (type.equals("price")) {
                                    callPriceWebServices(text);
                                }

                                if (type.equals("stock")) {
                                    callStockWebServices(text);
                                }
                            }
                            try {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                if (text.length() > 2) {

                    if (type.equals("price")) {
                        callPriceWebServices(text);
                    }

                    if (type.equals("stock")) {
                        callStockWebServices(text);
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void callPriceWebServices(String product_name) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.PRICE_SEARCH);
        map.put("user_id", sharedUtil.getUserDetails().getUser_id());
        map.put("product_name", product_name);
        new MyVolleyPostMethod(SearchActivity.this, map, ServerConstants.ServiceCode.PRICE_SEARCH, false);
    }

    private void callStockWebServices(String product_name) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.STOCK_SEARCH);
        map.put("user_id", sharedUtil.getUserDetails().getUser_id());
        map.put("product_name", product_name);
        new MyVolleyPostMethod(SearchActivity.this, map, ServerConstants.ServiceCode.STOCK_SEARCH, false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.PRICE_SEARCH:
                try {
                    Logs.showLogE(TAG, response);
                    CNP cnp = ResponseUtil.getPriceList(response);
                    if (cnp.getError_code() == 1) {
                        products.clear();
                        products.addAll(cnp.getProducts());
                        if (products.size() != 0) {
                            txt_no_data_found.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter1 = new ProductPriceRecyclerAdapter(SearchActivity.this, products);
                            recyclerView.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
                        }
                    } else {
                        txt_no_data_found.setVisibility(View.VISIBLE);
                        txt_no_data_found.setText("No data found");
                        recyclerView.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ServerConstants.ServiceCode.STOCK_SEARCH:
                try {
                    Logs.showLogE(TAG, response);
                    CNP cnp = ResponseUtil.getStockList(response);
                    if (cnp.getError_code() == 1) {
                        products.clear();
                        products.addAll(cnp.getProducts());
                        if (products.size() != 0) {
                            txt_no_data_found.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter2 = new ProductStockRecyclerAdapter(SearchActivity.this, products);
                            recyclerView.setAdapter(adapter2);
                            adapter2.notifyDataSetChanged();
                        }
                    } else {
                        txt_no_data_found.setVisibility(View.VISIBLE);
                        txt_no_data_found.setText("No data found");
                        recyclerView.setVisibility(View.GONE);
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

            case ServerConstants.ServiceCode.PRICE_SEARCH:
                Logs.showLogE(TAG, response);
                break;

            case ServerConstants.ServiceCode.STOCK_SEARCH:
                Logs.showLogE(TAG, response);
                break;

        }
    }
}
