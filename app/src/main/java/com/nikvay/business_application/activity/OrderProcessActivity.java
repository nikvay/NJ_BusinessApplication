package com.nikvay.business_application.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.OrderProcessListAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.QuotationListModel;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderProcessActivity extends AppCompatActivity implements VolleyCompleteListener {
    private RecyclerView recyclerOrderProcess;
    private ArrayList<QuotationListModel> arrayList = new ArrayList<>();
    private OrderProcessListAdapter adapter;
    private UserData userData;
    private QuotationListModel model;
    public static boolean toRefresh = false;
    private FloatingActionButton fab;
    ImageView iv_empty_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        iv_empty_list = findViewById(R.id.iv_empty_list);

        Toolbar toolbar = findViewById(R.id.toolbarOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toRefresh) {
            callQuotationWS();
            toRefresh = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
            }
        }
        return true;
    }

    private void initialize() {
        userData = new UserData(getApplicationContext());
        fab = findViewById(R.id.fab);
        recyclerOrderProcess = findViewById(R.id.recyclerOrderProcess);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerOrderProcess.setLayoutManager(manager);
        events();
        callQuotationWS();
    }

    private void events() {

        recyclerOrderProcess.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    fab.hide();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                if (dy == 0) {
                    fab.show();

                } else if (dy > 0) {

                    fab.show();
                } else if (dy < 0) {
                    fab.hide();
                }


                super.onScrolled(recyclerView, dx, dy);


            }
        });


    }

    private void refreshWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.QUOTATION_LIST);
        map.put("sales_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        map.put("status", "4");
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.QUOTATION_LIST, false);

    }

    private void callQuotationWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.QUOTATION_LIST);
        map.put("sales_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        map.put("status", "4");
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.QUOTATION_LIST, true);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.QUOTATION_LIST: {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("quotetion_list");
                        if (jsonArray.length() > 0) {
                            fab.show();
                            arrayList = new ArrayList<>();
                            arrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jddata = jsonArray.getJSONObject(i);
                                JSONArray jArray = jddata.getJSONArray("product");
                                JSONObject jdata = jArray.getJSONObject(0);
                                String quote_num = jdata.getString("quote_num");
                                String cust_name = jdata.getString("cust_name");
                                String tot_amount = jdata.getString("tot_amount");
                                String status = jdata.getString("status");
                                status = getStatusMeaning(status);
                                model = new QuotationListModel();
                                model.setQuote_num(quote_num);
                                model.setCust_name(cust_name);
                                model.setTot_amount(tot_amount);
                                model.setStatus(status);
                                arrayList.add(model);
                            }
                            //    textTotalQuotation.setText(String.valueOf(arrayList.size()));
                            adapter = new OrderProcessListAdapter(getApplicationContext(), arrayList);
                            recyclerOrderProcess.setAdapter(adapter);
                        } else {
                            if (arrayList.size() > 0) {
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                            iv_empty_list.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                            //    textTotalQuotation.setText("0");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }

    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }

    private String getStatusMeaning(String status) {
        switch (status) {
            case "0": {
                return StaticContent.QuotationStatusCode.ZERO;
            }
            case "1": {
                return StaticContent.QuotationStatusCode.ONE;
            }
            case "2": {
                return StaticContent.QuotationStatusCode.TWO;
            }
            case "3": {
                return StaticContent.QuotationStatusCode.THREE;
            }
            case "4": {
                return StaticContent.QuotationStatusCode.FOUR;
            }
            case "5": {
                return StaticContent.QuotationStatusCode.FIVE;
            }
        }
        return "N/A";
    }

}
