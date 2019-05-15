package com.nikvay.business_application.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.QuotationListAdapter;
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

public class QuotationListActivity extends AppCompatActivity implements VolleyCompleteListener {
    private TextView textTotalQuotation;
    private RecyclerView recyclerQuotation;
    ImageView iv_empty_list;
    private UserData userData;
    private QuotationListModel model;
    private ArrayList<QuotationListModel> arrayList = new ArrayList<>();
    private QuotationListAdapter adapter;
    private EditText editSearchQuotation;
    public static boolean toRefresh = false;
    private Spinner spinnerQuotationStatus;
    private FloatingActionButton fab;

//    EditText edit_tv_status;
    TextView edit_tv_status;
    String statusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        Intent intent = getIntent();
        if (intent != null) {
            statusName = intent.getStringExtra("STATUS");
            edit_tv_status.setText(statusName);
        }
        callQuotationWS();
        events();
        localBrodcastInitialize();
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initialize() {
        userData = new UserData(getApplicationContext());
        edit_tv_status = findViewById(R.id.edit_tv_status);
        editSearchQuotation = findViewById(R.id.editSearchQuotation);
        textTotalQuotation = findViewById(R.id.textTotalQuotation);
        spinnerQuotationStatus = findViewById(R.id.spinnerQuotationStatus);
        spinnerQuotationStatus.setSelection(5);
        recyclerQuotation = findViewById(R.id.recyclerQuotation);
        iv_empty_list = findViewById(R.id.iv_empty_list);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        fab = findViewById(R.id.fab);
        recyclerQuotation.setLayoutManager(manager);

    }

    private void events() {

        editSearchQuotation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter(editSearchQuotation.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit_tv_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter(edit_tv_status.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerQuotationStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callQuotationWS();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerQuotation.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        map.put("status", String.valueOf(spinnerQuotationStatus.getSelectedItemPosition()));
        new MyVolleyPostMethod(QuotationListActivity.this, map, ServerConstants.ServiceCode.QUOTATION_LIST, false);

    }

    private void callQuotationWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.QUOTATION_LIST);
        map.put("sales_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        map.put("status",statusName);
//        map.put("status", String.valueOf(spinnerQuotationStatus.getSelectedItemPosition()));
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
                                String date = jdata.getString("date");
                                status = getStatusMeaning(status);
                                model = new QuotationListModel();
                                model.setQuote_num(quote_num);
                                model.setCust_name(cust_name);
                                model.setTot_amount(tot_amount);
                                model.setStatus(status);
                                model.setDate(date);
                                arrayList.add(model);
                            }
                            textTotalQuotation.setText(String.valueOf(arrayList.size()));
                            adapter = new QuotationListAdapter(getApplicationContext(), arrayList);
                            recyclerQuotation.setAdapter(adapter);
                        } else {
                            if (arrayList.size() > 0) {
                                arrayList.clear();
//                                iv_empty_list.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
//                            iv_empty_list.setVisibility(View.VISIBLE);
                            textTotalQuotation.setText("0");

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
            case "6": {
                return StaticContent.QuotationStatusCode.SIX;
            }
        }
        return "N/A";
    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toRefresh) {
            refreshWS();
            toRefresh = false;
        }
    }
}
