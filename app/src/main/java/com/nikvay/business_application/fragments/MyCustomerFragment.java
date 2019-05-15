package com.nikvay.business_application.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.AddCustomerActivity;
import com.nikvay.business_application.adapter.MyCustomerAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.MyCustomerModel;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.volley_support.MyVolleyPostFragmentMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.VIBRATOR_SERVICE;

@SuppressLint("ValidFragment")
public class  MyCustomerFragment extends Fragment implements VolleyCompleteListener {
    private Context mContext;
    private View rootView;
    private VolleyCompleteListener volleyCompleteListener;
    private SwipeRefreshLayout refreshMyCustomer;
    private RecyclerView recyclerMyCustomer;
    private Button btnAddCustomerMyCustomer;
    private UserData userData;
    private ArrayList<MyCustomerModel> arrayList;
    private MyCustomerModel model;
    private MyCustomerAdapter adapter;
    public static boolean isToRefresh = false;
    private TextView textTotalCustomer;
    private EditText editSearchCustomer;
    private FloatingActionButton fab;
    public MyCustomerFragment(Context mContext) {
        this.mContext = mContext;
        this.volleyCompleteListener = this;
        this.userData = new UserData(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_customer, container, false);
        initialize();
        return rootView;
    }

    private void initialize() {
        VibrateOnClick.vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
        fab=rootView.findViewById(R.id.fab);
        refreshMyCustomer = rootView.findViewById(R.id.refreshMyCustomer);
        btnAddCustomerMyCustomer =  rootView.findViewById(R.id.btnAddCustomerMyCustomer);
        editSearchCustomer =rootView.findViewById(R.id.editSearchCustomer);
        textTotalCustomer = rootView.findViewById(R.id.textTotalCustomer);
        recyclerMyCustomer =  rootView.findViewById(R.id.recyclerMyCustomer);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerMyCustomer.setLayoutManager(manager);


        recyclerMyCustomer.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        callMyCustomerWS();
        events();
    }

    private void callMyCustomerWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.MY_CUSTOMER_LIST);
        map.put("sale_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        new MyVolleyPostFragmentMethod(getActivity(), volleyCompleteListener, map, ServerConstants.ServiceCode.MY_CUSTOMER_LIST, true);
    }

    private void events() {
        editSearchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(editSearchCustomer.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        refreshMyCustomer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callMyCustomerWS();
                refreshMyCustomer.setRefreshing(false);
            }
        });
        btnAddCustomerMyCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                startActivity(new Intent(mContext, AddCustomerActivity.class));

            }
        });
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.MY_CUSTOMER_LIST: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("customer_list");
                        if (jsonArray.length() > 0) {
                            fab.show();
                            arrayList = new ArrayList<>();
                            arrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String c_id = jdata.getString("c_id");
                                String sale_person_id = jdata.getString("sale_person_id");
                                String date_of_registration = jdata.getString("date_of_registration");
                                String company_name = jdata.getString("company_name");
                                String billing_address = jdata.getString("billing_address");
                                String location = jdata.getString("location");
                                String state = jdata.getString("state");
                                String pincode = jdata.getString("pincode");
                                String address = location + " " + state + " " + pincode;
                                String billing_contact_person = jdata.getString("billing_contact_person");
                                String tel_no = jdata.getString("tel_no");
                                String cell_no = jdata.getString("cell_no");
                                String email_id = jdata.getString("email_id");
                                String billing_GST_no = jdata.getString("billing_GST_no");
                                String packing_charges = jdata.getString("packing_charges");
                                String insurance_charges = jdata.getString("insurance_charges");
                                String term_of_payment = jdata.getString("term_of_payment");
                                String freight_terms = jdata.getString("freight_terms");
                                String discount = jdata.getString("discount");
                                String outstanding_amount = jdata.getString("outstanding_amount");
                                String budget=jdata.getString("budget");
                               //String sale=jdata.getString("sale");

                                JSONArray saleArray = jdata.getJSONArray("sale");

                                model = new MyCustomerModel();
                                if (saleArray.length() > 0) {

                                    JSONObject salesdata = saleArray.getJSONObject(0);
                                    String year1 = salesdata.getString("year1");
                                    String sale_count1 = salesdata.getString("sale_count1");
                                    String year2 = salesdata.getString("year2");
                                    String sale_count2 = salesdata.getString("sale_count2");
                                    String year3 = salesdata.getString("year3");
                                    String sale_count3 = salesdata.getString("sale_count3");

                                    model.setYear1(year1);
                                    model.setYear2(year2);
                                    model.setYear3(year3);
                                    model.setSale_count1(sale_count1);
                                    model.setSale_count2(sale_count2);
                                    model.setSale_count3(sale_count3);
                                }

                                model.setC_id(c_id);
                                model.setSale_person_id(sale_person_id);
                                model.setDate_of_registration(date_of_registration);
                                model.setDiscount(discount);
                                model.setOutstanding_amount(outstanding_amount.equals("null") ? "0" : outstanding_amount);
                                model.setCompany_name(company_name);
                                model.setBilling_address(billing_address);
                                model.setLocation(location);
                                model.setState(state);
                                model.setPincode(pincode);
                                model.setAddress(address);
                                model.setBilling_contact_person(billing_contact_person);
                                model.setTel_no(tel_no);
                                model.setCell_no(cell_no);
                                model.setEmail_id(email_id);
                                model.setBilling_GST_no(billing_GST_no);
                                model.setPacking_charges(packing_charges);
                                model.setInsurance_charges(insurance_charges);
                                model.setTerm_of_payment(term_of_payment);
                                model.setFreight_terms(freight_terms);
                                model.setBudget(budget.equalsIgnoreCase("null")?"NA":budget);
                            //    model.setSale(sale);
                                model.setSelected(false);
                                arrayList.add(model);
                            }
                            adapter = new MyCustomerAdapter(mContext, arrayList, false);
                            recyclerMyCustomer.setAdapter(adapter);
                            textTotalCustomer.setText(String.valueOf(arrayList.size()));
                        } else {
                            textTotalCustomer.setText("0");
                            Toast.makeText(mContext, "No Customer Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        textTotalCustomer.setText("0");
                        Toast.makeText(mContext, "No Customer Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    textTotalCustomer.setText("0");
                    Toast.makeText(mContext, "No Customer Found", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isToRefresh) {
            isToRefresh = false;
            callMyCustomerWS();
        }
    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }
}
