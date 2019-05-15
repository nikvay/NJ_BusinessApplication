package com.nikvay.business_application.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.fragments.MyCustomerFragment;
import com.nikvay.business_application.model.MyCustomerModel;
import com.nikvay.business_application.utils.MessageDialog;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.utils.SuccessDialogClosed;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AddCustomerActivity extends AppCompatActivity implements VolleyCompleteListener, SuccessDialogClosed {
    private AutoCompleteTextView textCompanyName,
            textBillingAddress,
            textLocation,
            textState,
            textPincode,
            textBillingContactPerson,
            textTeliphoneNumber,
            textMobileNumber,
            textEmailId,
            textBillingGSTNumber,
            textPackingCharges,
            textInsuranceCharges,
            textTermOfPayment,
            textFreightTerm,
            textFreightPerson,
            textReference,
            textDiscountLimit,
            textBudget;
    private Button btnAdd;
    private UserData userData;
    private SuccessDialog successDialog;
    private MyCustomerModel customerModel;
    private String mTitle = "Add Customer";
    private TextInputLayout inputLayout;
    MessageDialog messageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            customerModel = (MyCustomerModel) bundle.getSerializable(StaticContent.IntentKey.CUSTOMER_DETAIL);
            mTitle = bundle.getString(StaticContent.IntentKey.ACTIVITY_TYPE);
        }
        getSupportActionBar().setTitle(mTitle);
        localBrodcastInitialize();
        initialize();
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

    private void initialize() {
        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        userData = new UserData(getApplicationContext());
        successDialog = new SuccessDialog(this, true);
        textCompanyName = findViewById(R.id.textCompanyName);
        textBillingAddress =  findViewById(R.id.textBillingAddress);
        textLocation =  findViewById(R.id.textLocation);
        textState = findViewById(R.id.textState);
        textPincode = findViewById(R.id.textPincode);
        textBillingContactPerson =findViewById(R.id.textBillingContactPerson);
        textTeliphoneNumber =  findViewById(R.id.textTeliphoneNumber);
        textMobileNumber =  findViewById(R.id.textMobileNumber);
        textEmailId = findViewById(R.id.textEmailId);
        textBillingGSTNumber = findViewById(R.id.textBillingGSTNumber);
        textPackingCharges =  findViewById(R.id.textPackingCharges);
        textInsuranceCharges =findViewById(R.id.textInsuranceCharges);
        textTermOfPayment =  findViewById(R.id.textTermOfPayment);
        textFreightTerm =  findViewById(R.id.textFreightTerm);
        textFreightPerson = findViewById(R.id.textFreightPerson);
        textReference =findViewById(R.id.textReference);
        textDiscountLimit =findViewById(R.id.textDiscountLimit);
        inputLayout = findViewById(R.id.inputLayout);
        textBudget=findViewById(R.id.textBudget);

        btnAdd =  findViewById(R.id.btnAdd);
        if (mTitle.equals(StaticContent.IntentValue.ACTIVITY_EDIT_CUSTOMER)) {
            textCompanyName.setText(customerModel.getCompany_name());
            textBillingAddress.setText(customerModel.getBilling_address());
            textLocation.setText(customerModel.getLocation());
            textState.setText(customerModel.getState());
            textPincode.setText(customerModel.getPincode());
            textBillingContactPerson.setText(customerModel.getBilling_contact_person());
            textDiscountLimit.setText(customerModel.getDiscount());
            textTeliphoneNumber.setText(customerModel.getTel_no());
            textMobileNumber.setText(customerModel.getCell_no());
            textEmailId.setText(customerModel.getEmail_id());
            textBillingGSTNumber.setText(customerModel.getBilling_GST_no());
            textPackingCharges.setText(customerModel.getPacking_charges());
            textInsuranceCharges.setText(customerModel.getInsurance_charges());
            textTermOfPayment.setText(customerModel.getTerm_of_payment());
            textFreightTerm.setText(customerModel.getFreight_terms());
            textBudget.setText(customerModel.getBudget());
            btnAdd.setText("Update");
            disableFields();
        }


        textPackingCharges.setText("1");
        textInsuranceCharges.setText("1");
        textFreightPerson.setText("To Pay Basis");

        messageDialog=new MessageDialog(AddCustomerActivity.this);


        events();
    }

    private void disableFields() {
        textCompanyName.setEnabled(false);
        textCompanyName.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textBillingAddress.setEnabled(false);
        textBillingAddress.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textLocation.setEnabled(false);
        textLocation.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textBillingGSTNumber.setEnabled(false);
        textBillingGSTNumber.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textState.setEnabled(false);
        textState.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textPincode.setEnabled(false);
        textPincode.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textTermOfPayment.setEnabled(false);
        textTermOfPayment.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textDiscountLimit.setEnabled(false);
        textDiscountLimit.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textBudget.setEnabled(false);
        textBudget.setTextColor(getResources().getColor(android.R.color.darker_gray));
}


    private void events() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                if (isValid()) {
                    if (mTitle.equals(StaticContent.IntentValue.ACTIVITY_EDIT_CUSTOMER)) {
                        callAddCustomerWS(ServerConstants.serverUrl.UPDATE_CUSTOMER, ServerConstants.ServiceCode.UPDATE_CUSTOMER);
                    } else {
                        callAddCustomerWS(ServerConstants.serverUrl.ADD_MY_CUSTOMER_LIST, ServerConstants.ServiceCode.ADD_MY_CUSTOMER_LIST);

                    }

                }
            }
        });
    }

    private boolean isValid() {
        if (!isEmpty(textCompanyName)) {
            textCompanyName.setError("Enter Company Name");
            textCompanyName.requestFocus();
            return false;
        } else {
            textCompanyName.setError(null);
            textCompanyName.clearFocus();
        }
        if (!isEmpty(textBillingAddress)) {
            textBillingAddress.setError("Enter Billing Address");
            textBillingAddress.requestFocus();
            return false;
        } else {
            textBillingAddress.setError(null);
            textBillingAddress.requestFocus();
        }
        if (!isEmpty(textLocation)) {
            textLocation.setError("Enter Location");
            textLocation.requestFocus();
            return false;
        } else {
            textLocation.setError(null);
            textLocation.requestFocus();
        }
        if (!isEmpty(textState)) {
            textState.setError("Enter State");
            textState.requestFocus();
            return false;
        } else {
            textState.setError(null);
            textState.requestFocus();
        }
        if (!isEmpty(textPincode) || textPincode.getText().toString().length() != 6) {
            textPincode.setError("Enter Valid Pin-code");
            textPincode.requestFocus();
            return false;
        } else {
            textPincode.setError(null);
            textPincode.requestFocus();
        }
        if (!isEmpty(textBillingContactPerson)) {
            textBillingContactPerson.setError("Enter Contact Person");
            textBillingContactPerson.requestFocus();
            return false;
        } else {
            textBillingContactPerson.setError(null);
            textBillingContactPerson.requestFocus();
        }
        if (!mTitle.equals(StaticContent.IntentValue.ACTIVITY_EDIT_CUSTOMER)) {

            if (!isEmpty(textDiscountLimit)) {
                textDiscountLimit.setError("Enter Discount Limit");
                textDiscountLimit.requestFocus();
                return false;
            } else {
                String check = textDiscountLimit.getText().toString();
                if (check.length() != 0) {
                    int g = 0;
                    if (check.length() == 1) {
                        if (check.equals(".")) {
                            textDiscountLimit.setText("0");
                            check = "0";
                        }
                    }
                    for (int i = 0; i < check.length(); i++) {
                        if (check.charAt(i) == '.') {
                            g++;
                            if (g > 1) {
                                textDiscountLimit.setText("0");
                                check = "0";
                                break;
                            }
                        }
                    }
                    if (Float.valueOf(check) > 55) {
                        textDiscountLimit.setError("0 to 55");
                        return false;
                    }
                } else {
                    textDiscountLimit.setText("0");
                }
                textDiscountLimit.setError(null);
                textDiscountLimit.requestFocus();
            }
        }

        if (!isEmpty(textMobileNumber) || textMobileNumber.getText().toString().length() != 10) {
            textMobileNumber.setError("Enter Valid Mobile Number");
            textMobileNumber.requestFocus();
            return false;
        } else {
            textMobileNumber.setError(null);
            textMobileNumber.requestFocus();
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(textEmailId.getText().toString()).matches()) {
            textEmailId.setError("Enter Valid Email Id");
            textEmailId.requestFocus();
            return false;
        } else {
            textEmailId.setError(null);
            textEmailId.requestFocus();
        }
        if (!isEmpty(textPackingCharges)) {
            textPackingCharges.setError("Enter Packing Charges");
            textPackingCharges.requestFocus();
            return false;
        } else {
            textPackingCharges.setError(null);
            textPackingCharges.requestFocus();
        }
        if (!isEmpty(textInsuranceCharges)) {
            textInsuranceCharges.setError("Enter Insurances Charges");
            textInsuranceCharges.requestFocus();
            return false;
        } else {
            textInsuranceCharges.setError(null);
            textInsuranceCharges.requestFocus();
        }
        if (!isEmpty(textFreightTerm)) {
            textFreightTerm.setError("Enter Freight Term");
            textFreightTerm.requestFocus();
            return false;
        } else {
            textFreightTerm.setError(null);
            textFreightTerm.requestFocus();
        }

        return true;
    }

    private boolean isEmpty(AutoCompleteTextView mTextView) {
        if (mTextView.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void callAddCustomerWS(String mUrl, int mCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, mUrl);
        map.put("sale_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        map.put("company_name", textCompanyName.getText().toString());
        map.put("billing_address", textBillingAddress.getText().toString());
        map.put("location", textLocation.getText().toString());
        map.put("state", textState.getText().toString());
        map.put("pincode", textPincode.getText().toString());
        map.put("billing_contact_person", textBillingContactPerson.getText().toString());
        map.put("discount", textDiscountLimit.getText().toString());
        if (!textTeliphoneNumber.getText().toString().isEmpty()) {
            map.put("tel_no", textTeliphoneNumber.getText().toString());
        }
        map.put("cell_no", textMobileNumber.getText().toString());
        map.put("email_id", textEmailId.getText().toString());
        if (!textBillingGSTNumber.getText().toString().isEmpty()) {
            map.put("billing_GST_no", textBillingGSTNumber.getText().toString());
        }
        map.put("packing_charges", textPackingCharges.getText().toString());
        map.put("insurance_charges", textInsuranceCharges.getText().toString());
        if (!textTermOfPayment.getText().toString().isEmpty()) {
            map.put("term_of_payment", textTermOfPayment.getText().toString());
        }
        map.put("freight_terms", textFreightTerm.getText().toString());
        if (!textFreightPerson.getText().toString().isEmpty()) {
            map.put("freight_reason", textFreightPerson.getText().toString());
        }
        if (!textReference.getText().toString().isEmpty()) {
            map.put("reference", textReference.getText().toString());
        }
        if (mTitle.equals(StaticContent.IntentValue.ACTIVITY_EDIT_CUSTOMER)) {
            map.put("c_id", customerModel.getC_id());
        }
        map.put("budget", textBudget.getText().toString().trim());
        new MyVolleyPostMethod(this, map, mCode, true);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.ADD_MY_CUSTOMER_LIST: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        //       Toast.makeText(getApplicationContext(), "Customer added successfully", Toast.LENGTH_SHORT).show();
                        successDialog.showDialog("Customer added successfully", true);

                    }else {
                       messageDialog.showDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case ServerConstants.ServiceCode.UPDATE_CUSTOMER: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        successDialog.showDialog("Updated successfully", true);

                    }
                    else
                    {
                        messageDialog.showDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }

    @Override
    public void dialogClosed(boolean mClosed) {
        MyCustomerFragment.isToRefresh = true;
        finish();
    }
}
