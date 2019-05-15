package com.nikvay.business_application.activity;

import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.CustomerUpdateBlankField;
import com.nikvay.business_application.utils.MessageDialog;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.utils.SuccessDialogClosed;
import com.nikvay.business_application.utils.ValidationUtil;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SendPIActivity extends AppCompatActivity implements VolleyCompleteListener, SuccessDialogClosed {
    private Button btnSubmitSendPi;


    private RadioButton radioYCA, radioNCA, radioDispatchEmail, radioCM, radioTCY, radioTCN, radioInspectionY, radioInspectionN;
    private AutoCompleteTextView textTransporter, textPaymentDetails, textEmail, textSpecs, textOther, textReference, textCell_No, textBilling_GST_No, textDelivery_Address, textTerm_Of_Payment;
    private TextInputLayout textReferenceTI, textCell_NoTI, textBilling_GST_NoTI, textDelivery_AddressTI, textTerm_Of_PaymentTI;
    private RadioGroup radioGroupDispatchEmail, radioGroupCMY;

    String profileComplete = "Yes";
    private String mQuotationNumber;
    private SuccessDialog successDialog;
    private LinearLayout ll_customerEdit;
    private CustomerUpdateBlankField customerUpdateBlankField;
    String customer_id, reference, cell_no, email_id, billing_GST_no, delivery_address, term_of_payment;
    String  mReference, mCell_no,mBilling_GST_no, mDelivery_address, mTerm_of_payment;
    String getReference, getCellNo, getBilling_Gst, getDelivery_Address, getTermOfPay, getEmail, getPaymentDetails, getTransporter, getOther, getSpecs;

    static int blankFieldCount = 0;

    MessageDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mQuotationNumber = getIntent().getExtras().getString(StaticContent.IntentType.QUOTATION_NUMBER);
        getSupportActionBar().setTitle(mQuotationNumber);
        initialize();
        callCustomerBlankFiled();

    }

    private void callCustomerBlankFiled() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.CUSTOMER_BLANK_FILED_LIST);
        map.put("quote_num", mQuotationNumber);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.CUSTOMER_BLANK_FILED_LIST, true);
    }

    private void initialize() {
        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        successDialog = new SuccessDialog(this, true);
        messageDialog = new MessageDialog(this);

        radioYCA = findViewById(R.id.radioYCA);
        radioNCA = findViewById(R.id.radioNCA);
        radioTCY = findViewById(R.id.radioTCY);
        radioTCN = findViewById(R.id.radioTCN);
        radioInspectionY = findViewById(R.id.radioInspectionY);
        radioInspectionN = findViewById(R.id.radioInspectionN);


        textReference = findViewById(R.id.textReference);
        textCell_No = findViewById(R.id.textCell_No);
        textBilling_GST_No = findViewById(R.id.textBilling_GST_No);
        textDelivery_Address = findViewById(R.id.textDelivery_Address);
        textTerm_Of_Payment = findViewById(R.id.textTerm_Of_Payment);


        textReferenceTI = findViewById(R.id.textReferenceTI);
        textCell_NoTI = findViewById(R.id.textCell_NoTI);
        textBilling_GST_NoTI = findViewById(R.id.textBilling_GST_NoTI);
        textDelivery_AddressTI = findViewById(R.id.textDelivery_AddressTI);
        textTerm_Of_PaymentTI = findViewById(R.id.textTerm_Of_PaymentTI);


        textTransporter = findViewById(R.id.textTransporter);
        textPaymentDetails = findViewById(R.id.textPaymentDetails);
        textEmail = findViewById(R.id.textEmail);
        textSpecs = findViewById(R.id.textSpecs);
        textOther = findViewById(R.id.textOther);

        radioGroupDispatchEmail = findViewById(R.id.radioGroupDispatchEmail);
        radioGroupCMY = findViewById(R.id.radioGroupCMY);
        ll_customerEdit = findViewById(R.id.ll_edit_customer);

        btnSubmitSendPi = findViewById(R.id.btnSubmitSendPi);


        events();

    }

    private void events() {
        btnSubmitSendPi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                callCheckBlankField();

                getReference = textReference.getText().toString().trim();
                getCellNo = textCell_No.getText().toString().trim();
                getBilling_Gst = textBilling_GST_No.getText().toString().trim();
                getDelivery_Address = textDelivery_Address.getText().toString().trim();
                getTermOfPay = textTerm_Of_Payment.getText().toString().trim();
                getEmail = textEmail.getText().toString().trim();
                getPaymentDetails = textPaymentDetails.getText().toString().trim();
                getTransporter = textTransporter.getText().toString().trim();
                getOther = textOther.getText().toString();
                getSpecs = textSpecs.getText().toString().trim();

                if (getTransporter.equalsIgnoreCase("")) {
                    textTransporter.setError("Enter Transporter");
                    textTransporter.requestFocus();
                } else if (getPaymentDetails.equalsIgnoreCase("")) {
                    textPaymentDetails.setError("Enter Payment Details");
                    textPaymentDetails.requestFocus();
                } else if (getEmail.equalsIgnoreCase("")) {
                    textEmail.setError("Enter Email");
                    textEmail.requestFocus();

                } else if (!ValidationUtil.emailCheck(textEmail.getText().toString().trim())) {
                    textEmail.setError("Invalid Email");
                    textEmail.requestFocus();
                } else if (getSpecs.equalsIgnoreCase("")) {
                    textSpecs.setError("Enter Specs");
                    textSpecs.requestFocus();
                } else if (getOther.equalsIgnoreCase("")) {
                    textOther.setError("Enter Other");
                    textOther.requestFocus();
                } else if (profileComplete.equalsIgnoreCase("Yes")) {
                    if (getReference.equalsIgnoreCase("")) {
                        messageDialog.showDialog("Customer Master Is" + "\n" + "Un-completed");
                    } else if (getCellNo.equalsIgnoreCase("")) {
                        messageDialog.showDialog("Customer Master Is" + "\n" + "Un-completed");

                    } else if (getBilling_Gst.equalsIgnoreCase("")) {
                        messageDialog.showDialog("Customer Master Is" + "\n" + "Un-completed");
                    } else if (getDelivery_Address.equalsIgnoreCase("")) {
                        messageDialog.showDialog("Customer Master Is" + "\n" + " Un-completed");
                    } else if (getTermOfPay.equalsIgnoreCase("")) {
                        messageDialog.showDialog("Customer Master Is" + "\n" + "Un-completed");
                    } else {
                         callCustomerUpdate();
                         callSendPiWS();
                    }

                } else if (profileComplete.equalsIgnoreCase("No")) {
                    if (getReference.equalsIgnoreCase("")) {
                        textReference.setError("Enter Reference");
                        textReference.requestFocus();

                    } else if (getCellNo.equalsIgnoreCase("")) {

                        textCell_No.setError("Enter Cell NO");
                        textCell_No.requestFocus();

                    } else if (getBilling_Gst.equalsIgnoreCase("")) {
                        textBilling_GST_No.setError("Enter Billing GST No");
                        textBilling_GST_No.requestFocus();

                    } else if (getDelivery_Address.equalsIgnoreCase("")) {
                        textDelivery_Address.setError("Enter Delivery Address");
                        textDelivery_Address.requestFocus();
                    } else if (getTermOfPay.equalsIgnoreCase("")) {
                        textTerm_Of_Payment.setError("Enter Term Of Payment");
                        textTerm_Of_Payment.requestFocus();
                    } else {
                        callCustomerUpdate();
                        callSendPiWS();
                    }


                }

            }

        });

        radioGroupDispatchEmail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int isChecked) {
                radioDispatchEmail = findViewById(isChecked);
                String emailDispatch = radioDispatchEmail.getText().toString().trim();
                if (emailDispatch.equalsIgnoreCase("Email")) {
                    textEmail.setText("");
                    textEmail.setEnabled(true);

                } else {
                    textEmail.setEnabled(false);
                    textEmail.setText(customerUpdateBlankField.getEmail_id());
                }


            }
        });
        radioGroupCMY.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int isChecked) {

                radioCM = findViewById(isChecked);
                profileComplete = radioCM.getText().toString().trim();
                if (profileComplete.equalsIgnoreCase("Yes")) {
                    ll_customerEdit.setVisibility(View.GONE);
                    callCheckBlankField();
                } else {
                    ll_customerEdit.setVisibility(View.VISIBLE);
                    callCheckBlankField();

                }
            }
        });


    }

    private void callCustomerUpdate() {
        if (reference.equalsIgnoreCase("")) {

            getReference = mReference;
        }

        if (cell_no.equalsIgnoreCase("")) {
            getCellNo = mCell_no;
        }

        if (billing_GST_no.equalsIgnoreCase("")) {

            getBilling_Gst = mBilling_GST_no;

        }

        if (delivery_address.equalsIgnoreCase("")) {
            getDelivery_Address = mDelivery_address;
        }
        if (term_of_payment.equalsIgnoreCase("")) {

            getTermOfPay = mTerm_of_payment;
        }

    }

    private void callCheckBlankField() {
        try {
            if (reference.equalsIgnoreCase("0") || reference.equalsIgnoreCase("null") || reference.equalsIgnoreCase("NA") || reference.equalsIgnoreCase("")) {

                blankFieldCount++;
            } else {

                textReferenceTI.setVisibility(View.GONE);
                textReference.setText(reference);
                mReference = "";

            }

            if (cell_no.equalsIgnoreCase("0") || cell_no.equalsIgnoreCase("null") || cell_no.equalsIgnoreCase("NA") || cell_no.equalsIgnoreCase("")) {
                blankFieldCount++;
            } else {
                textCell_NoTI.setVisibility(View.GONE);
                textCell_No.setText(cell_no);
                mCell_no = "";


            }
            if (billing_GST_no.equalsIgnoreCase("0") || billing_GST_no.equalsIgnoreCase("null") || billing_GST_no.equalsIgnoreCase("NA") || billing_GST_no.equalsIgnoreCase("")) {
                blankFieldCount++;


            } else {

                textBilling_GST_NoTI.setVisibility(View.GONE);
                textBilling_GST_No.setText(billing_GST_no);
                mBilling_GST_no = "";

            }


            if (delivery_address.equalsIgnoreCase("0") || delivery_address.equalsIgnoreCase("null") || delivery_address.equalsIgnoreCase("NA") || delivery_address.equalsIgnoreCase("")) {
                blankFieldCount++;
            } else {
                textDelivery_AddressTI.setVisibility(View.GONE);
                textDelivery_Address.setText(delivery_address);
                mDelivery_address = "";


            }
            if (term_of_payment.equalsIgnoreCase("0") || term_of_payment.equalsIgnoreCase("null") || term_of_payment.equalsIgnoreCase("NA") || term_of_payment.equalsIgnoreCase("")) {
                blankFieldCount++;
            } else {
                textTerm_Of_PaymentTI.setVisibility(View.GONE);
                textTerm_Of_Payment.setText(term_of_payment);
                mTerm_of_payment = "";


            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void callSendPiWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.SEND_PI);
        map.put("quote_num", mQuotationNumber);
        map.put("is_cpo_attached", radioYCA.isSelected() ? "yes" : "no");
        map.put("transpoter", getTransporter);
        map.put("payment_details", getPaymentDetails);
        map.put("dispatched_type", radioGroupDispatchEmail.isSelected() ? "email" : "master");
        map.put("dispatched_email", getEmail);
        map.put("is_customer_master_complete", radioGroupCMY.isSelected() ? "yes" : "no");
        map.put("customer_id", customerUpdateBlankField.getCustomer_id());
        map.put("reference", getReference);
        map.put("cell_no", getCellNo);
        map.put("billing_GST_no", getBilling_Gst);
        map.put("delivery_address", getDelivery_Address);
        map.put("term_of_payment", getTermOfPay);
        map.put("is_tc_warranty_letter", radioTCY.isSelected() ? "yes" : "no");
        map.put("space_related", getSpecs);
        map.put("inspection", radioInspectionY.isSelected() ? "yes" : "no");
        map.put("others", getOther);

        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.SEND_PI, true);
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
            case ServerConstants.ServiceCode.SEND_PI: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        //       Toast.makeText(getApplicationContext(), "Customer added successfully", Toast.LENGTH_SHORT).show();
                        successDialog.showDialog("Pi submitted", true);
                        OrderProcessActivity.toRefresh = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }


            case ServerConstants.ServiceCode.CUSTOMER_BLANK_FILED_LIST: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        JSONObject jsonObjectSub = jsonObject.getJSONObject("user_details");
                        if (jsonObjectSub.length() > 0) {

                            customer_id = jsonObjectSub.getString("customer_id").trim();
                            reference = jsonObjectSub.getString("reference").trim();
                            cell_no = jsonObjectSub.getString("cell_no").trim();
                            email_id = jsonObjectSub.getString("email_id");
                            billing_GST_no = jsonObjectSub.getString("billing_GST_no").trim();
                            delivery_address = jsonObjectSub.getString("delivery_address").trim();
                            term_of_payment = jsonObjectSub.getString("term_of_payment").trim();


                            customerUpdateBlankField = new CustomerUpdateBlankField();
                            customerUpdateBlankField.setCustomer_id(customer_id);
                            customerUpdateBlankField.setReference(reference);
                            customerUpdateBlankField.setCell_no(cell_no);
                            customerUpdateBlankField.setEmail_id(email_id);
                            customerUpdateBlankField.setBilling_GST_no(billing_GST_no);
                            customerUpdateBlankField.setDelivery_address(delivery_address);
                            customerUpdateBlankField.setTerm_of_payment(term_of_payment);


                        }

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
        finish();
    }
}
