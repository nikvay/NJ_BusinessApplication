package com.nikvay.business_application.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.OutstandingAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.OutstandingModel;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class  OutstandingActivity extends AppCompatActivity implements VolleyCompleteListener {
    private RecyclerView recyclerOutstanding;
    private UserData userData;
    private Toolbar toolbar;
    private OutstandingModel outstandingModel;
    private ArrayList<OutstandingModel> arrayList = new ArrayList<>();
    private OutstandingAdapter adapter;
    private FloatingActionButton fab;
    private  ImageView iv_empty_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);


        toolbar =  findViewById(R.id.toolbarOutstanding);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    private void initialize() {
        iv_empty_list =  findViewById(R.id.iv_empty_list);
        userData = new UserData(getApplicationContext());
        fab=findViewById(R.id.fab);
        recyclerOutstanding =findViewById(R.id.recyclerOutstanding);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerOutstanding.setLayoutManager(manager);
        callOutstandingWS();
        events();
    }

    private void events() {

        recyclerOutstanding.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1))
                {
                    fab.hide();
                }
                else if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                if(dy==0)
                {
                    fab.show();

                }
                else if(dy>0){

                    fab.show();
                }
                else if(dy<0)
                {
                    fab.hide();
                }



                super.onScrolled(recyclerView,dx,dy);



            }
        });




    }

    private void callOutstandingWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.GET_OUTSTANDING_AMOUNT);
        map.put("sales_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.GET_OUTSTANDING_AMOUNT, true);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.GET_OUTSTANDING_AMOUNT: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("customer_data");
                        if (jsonArray.length() != 0) {
                            fab.show();
                            arrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String c_id = jdata.getString("c_id");
                                String company_name = jdata.getString("company_name");
                                String email_id = jdata.getString("email_id");
                                String amount = jdata.getString("total_amount");
                                outstandingModel=new OutstandingModel();
                                outstandingModel.setC_id(c_id);
                                outstandingModel.setCompany_name(company_name);
                                outstandingModel.setEmail_id(email_id);
                                outstandingModel.setAmount(amount);
                                arrayList.add(outstandingModel);
                            }
                            adapter = new OutstandingAdapter(arrayList, getApplicationContext());
                            recyclerOutstanding.setAdapter(adapter);
                        } else {
                            iv_empty_list.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), "Empty list", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Empty list", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }
    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }
}
