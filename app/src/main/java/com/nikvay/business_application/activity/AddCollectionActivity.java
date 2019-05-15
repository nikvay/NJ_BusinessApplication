package com.nikvay.business_application.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.MyCustomerAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.MyCustomerModel;
import com.nikvay.business_application.utils.MyCustomerResponse;
import com.nikvay.business_application.utils.SelectCustomerInterface;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.utils.SuccessDialogClosed;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCollectionActivity extends AppCompatActivity implements VolleyCompleteListener, SelectCustomerInterface,SuccessDialogClosed {
    private AutoCompleteTextView textCustomerNameCollection,
            textAmountCollection,
            textBillCollection,
            textCompanyNameCollection;
    private Button btnSubmitCollection;
    private UserData userData;
    private TextView textSelectCustomerVisitC;
    private ImageView textSelectCustomerCHC;
    private MyCustomerResponse myCustomerResponse;
    private ArrayList<MyCustomerModel> arrayListC = new ArrayList<>();
    private MyCustomerAdapter adapterC;
    private Dialog selectCustomerDialog;
    private Button btnOkDialogSC, btnCancelDialogSC;
    private RecyclerView recyclerDialogSC;
    private String mCustomerNameC = null;
    private EditText editSearchC;
    private SuccessDialog successDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        localBrodcastInitialize();
        initialize();
    }

    private void initialize() {
        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        userData = new UserData(getApplicationContext());
        successDialog = new SuccessDialog(this,true);
        myCustomerResponse = new MyCustomerResponse(getApplicationContext());
        selectCustomerDialog = new Dialog(this);
        selectCustomerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectCustomerDialog.setContentView(R.layout.dialog_select_customer);
        selectCustomerDialog.setCancelable(false);
        btnOkDialogSC =  selectCustomerDialog.findViewById(R.id.btnOkDialogSC);
        btnCancelDialogSC =  selectCustomerDialog.findViewById(R.id.btnCancelDialogSC);
        editSearchC = selectCustomerDialog.findViewById(R.id.editSearchC);
        recyclerDialogSC =selectCustomerDialog.findViewById(R.id.recyclerDialogSC);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerDialogSC.setLayoutManager(manager);
        textCustomerNameCollection = findViewById(R.id.textCustomerNameCollection);
        textCustomerNameCollection.setEnabled(false);
        textAmountCollection =  findViewById(R.id.textAmountCollection);
        textBillCollection =  findViewById(R.id.textBillCollection);
        btnSubmitCollection =findViewById(R.id.btnSubmitCollection);
        textSelectCustomerVisitC = findViewById(R.id.textSelectCustomerVisitC);
        textSelectCustomerCHC =  findViewById(R.id.textSelectCustomerCHC);
        textCompanyNameCollection =  findViewById(R.id.textCompanyNameCollection);
        events();
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

    private void events() {
        btnSubmitCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                if (isValid()) {
                    callCollectionWS();
                }
            }
        });
        btnCancelDialogSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearchC.setText("");
                mCustomerNameC = null;
                textCustomerNameCollection.setText(null);
                selectCustomerDialog.dismiss();
            }
        });
        btnOkDialogSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearchC.setText("");
                if (mCustomerNameC != null) {
                    textCustomerNameCollection.setText(mCustomerNameC);
                }
                selectCustomerDialog.dismiss();
            }
        });
        textSelectCustomerVisitC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMyCustomerWS();
            }
        });
        textSelectCustomerCHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                callMyCustomerWS();
            }
        });

        editSearchC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterC.getFilter().filter(editSearchC.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void callMyCustomerWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.MY_CUSTOMER_LIST);
        map.put("sale_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.MY_CUSTOMER_LIST, true);
    }

    private boolean isEmpty(AutoCompleteTextView mTextView) {
        if (mTextView.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isValid() {
        if (!isEmpty(textCustomerNameCollection)) {
            textCustomerNameCollection.setError("Enter Customer Name");
            textCustomerNameCollection.requestFocus();
            return false;
        } else {
            textCustomerNameCollection.setError(null);
            textCustomerNameCollection.clearFocus();
        }
        if (!isEmpty(textAmountCollection)) {
            textAmountCollection.setError("Enter Amount");
            textAmountCollection.requestFocus();
            return false;
        } else {
            textAmountCollection.setError(null);
            textAmountCollection.requestFocus();
        }
        if (!isEmpty(textBillCollection)) {
            textBillCollection.setError("Enter Bill number");
            textBillCollection.requestFocus();
            return false;
        } else {
            textBillCollection.setError(null);
            textBillCollection.requestFocus();
        }
        return true;
    }

    private void callCollectionWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.COLLECTION);
        map.put("sales_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        map.put("cust_name", textCustomerNameCollection.getText().toString());
        map.put("company_name", textCompanyNameCollection.getText().toString());
        map.put("amount", textAmountCollection.getText().toString());
        map.put("bill_no", textBillCollection.getText().toString());
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.COLLECTION, true);
    }

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
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.COLLECTION: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        CommonVisitCollectionActivity.isAdded = true;
                        // Toast.makeText(getApplicationContext(), "Collection added successfully", Toast.LENGTH_SHORT).show();
                        successDialog.showDialog("Collection added successfully", true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case ServerConstants.ServiceCode.MY_CUSTOMER_LIST: {
                if (arrayListC != null) {
                    arrayListC.clear();
                }
                arrayListC = myCustomerResponse.getCustomerResponse(response);
                if (arrayListC != null) {
                    adapterC = new MyCustomerAdapter(getApplicationContext(), arrayListC, true, this, true);
                    selectCustomerDialog.show();
                    Window window = selectCustomerDialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    recyclerDialogSC.setAdapter(adapterC);
                }
                break;
            }

        }

    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }

    @Override
    public void getCustomerName(String mCustomerName) {
        mCustomerNameC = mCustomerName;
    }

    @Override
    public void getCustomerDetail(MyCustomerModel customerModel) {
        textCompanyNameCollection.setText(customerModel.getCompany_name());
    }

    @Override
    public void dialogClosed(boolean mClosed) {
        finish();
    }
}
