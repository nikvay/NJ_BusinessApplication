package com.nikvay.business_application.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.QuotationEditAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.ViewQuotationModel;
import com.nikvay.business_application.utils.QuotationUpdateNotifier;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.volley_support.AppController;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditQuotationActivity extends AppCompatActivity implements VolleyCompleteListener, QuotationUpdateNotifier {
    private Toolbar toolbar;
    private RecyclerView recyclerEditQuotation;
    private AppController appController;
    private ArrayList<ViewQuotationModel> arrayList = new ArrayList<>();
    private QuotationEditAdapter adapter;
    private EditText editDiscountEdit;
    private Button btnDiscountEdit,
            btnSaveEdit;
    private String mDis_limit = null, discount_limit = null;
    private String mQuotationNumber = null;
    private String mUpdatedQuantity = null;
    private int mUpdatedPosition;
    private SuccessDialog successDialog;
    private AutoCompleteTextView textAddressEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quotation);
        toolbar = findViewById(R.id.toolbarEditQ);
        setSupportActionBar(toolbar);
        discount_limit = getIntent().getExtras().getString(StaticContent.IntentKey.DISCOUNT_LIMIT);
        mQuotationNumber = getIntent().getExtras().getString(StaticContent.IntentKey.QUOTATION_NAME);
        getSupportActionBar().setTitle(mQuotationNumber);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    private void initialize() {
        successDialog = new SuccessDialog(this);
        appController = (AppController) getApplicationContext();
        arrayList = (ArrayList<ViewQuotationModel>) appController.getMyData();
        btnSaveEdit =  findViewById(R.id.btnSaveEdit);
        textAddressEdit =  findViewById(R.id.textAddressEdit);
        textAddressEdit.setText(arrayList.get(0).getNew_billing_address());
        editDiscountEdit = findViewById(R.id.editDiscountEdit);
        editDiscountEdit.setHint("0" + " to " + discount_limit);
        btnDiscountEdit =  findViewById(R.id.btnDiscountEdit);
        recyclerEditQuotation =  findViewById(R.id.recyclerEditQuotation);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerEditQuotation.setLayoutManager(manager);
        adapter = new QuotationEditAdapter(this, arrayList);
        recyclerEditQuotation.setAdapter(adapter);
        events();
    }

    private void events() {
        btnDiscountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    String check = editDiscountEdit.getText().toString();
                    if(check.length()!=0) {
                        int g = 0;
                        if(check.length()==1){
                            if(check.equals(".")){
                                editDiscountEdit.setText(discount_limit);
                            }
                        }
                        for (int i = 0; i < check.length(); i++) {
                            if (check.charAt(i)=='.') {
                                g++;
                                if (g > 1) {
                                    editDiscountEdit.setText(discount_limit);
                                    break;
                                }
                            }
                        }
                    }else {
                        editDiscountEdit.setText(discount_limit);
                    }

                    if (Float.valueOf(editDiscountEdit.getText().toString()) <= Float.valueOf(discount_limit)) {
                        editDiscountEdit.setError(null);
                        callUpdateDiscountWS();
                    } else {
                        editDiscountEdit.setError("0" + " to " + discount_limit);
                        Toast.makeText(getApplicationContext(), "0" + " to " + discount_limit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No product to apply discount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textAddressEdit.getText().toString().isEmpty() || textAddressEdit.getText().toString().length() < 5 || textAddressEdit.getText().toString().equals("N/A")) {
                    textAddressEdit.setError("Enter Valid Address");
                } else {
                    if (arrayList.size() > 0) {
                        textAddressEdit.setError(null);
                        HashMap<String, String> map = new HashMap<>();
                        map.put(ServerConstants.URL, ServerConstants.serverUrl.UPDATE_ADDRESS);
                        map.put("quote_num", mQuotationNumber);
                        map.put("new_billing_address", textAddressEdit.getText().toString());
                        new MyVolleyPostMethod(EditQuotationActivity.this, map, ServerConstants.ServiceCode.UPDATE_ADDRESS, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "No product for billing address", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void callUpdateDiscountWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.UPDATE_DISCOUNT);
        map.put("quote_num", mQuotationNumber);
        map.put("discount_value", editDiscountEdit.getText().toString());
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.UPDATE_DISCOUNT, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appController.clearData();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                appController.clearData();
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.UPDATE_QUANTITY: {
                JSONObject jsonObject = null;
                String error_code = null;
                String msg = null;
                try {
                    jsonObject = new JSONObject(response);
                    error_code = jsonObject.getString("error_code");
                    msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        ViewQuotationActivity.isUpdated = true;
                        successDialog.showDialog("Quantity Updated", true);
                        int mPrice = Integer.valueOf(arrayList.get(mUpdatedPosition).getPrice());
                        int mQuantity = Integer.valueOf(mUpdatedQuantity);
                        int totalPrice = mPrice * mQuantity;
                        arrayList.get(mUpdatedPosition).setmQbasedPrice(String.valueOf(totalPrice));
                        arrayList.get(mUpdatedPosition).setProduct_qty(mUpdatedQuantity);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                }

                break;
            }
            case ServerConstants.ServiceCode.DELETE_PRODUCT: {
                JSONObject jsonObject = null;
                String error_code = null;
                String msg = null;
                try {
                    jsonObject = new JSONObject(response);
                    error_code = jsonObject.getString("error_code");
                    msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        ViewQuotationActivity.isUpdated = true;
                        successDialog.showDialog("Product Deleted", true);
                        arrayList.remove(mUpdatedPosition);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                }

                break;
            }
            case ServerConstants.ServiceCode.UPDATE_DISCOUNT: {
                JSONObject jsonObject = null;
                String error_code = null;
                String msg = null;
                try {
                    jsonObject = new JSONObject(response);
                    error_code = jsonObject.getString("error_code");
                    msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        ViewQuotationActivity.isUpdated = true;
                        successDialog.showDialog("Discount Applied", true);
                        if (arrayList.size() != 0) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.get(i).setDiscount_value(editDiscountEdit.getText().toString());
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                }

                break;
            }
            case ServerConstants.ServiceCode.UPDATE_ADDRESS: {
                JSONObject jsonObject = null;
                String error_code = null;
                String msg = null;
                try {
                    jsonObject = new JSONObject(response);
                    error_code = jsonObject.getString("error_code");
                    msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        successDialog.showDialog("Address Updated Successfully", true);
                        ViewQuotationActivity.isUpdated = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                }

                break;
            }

        }
    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }

    @Override
    public void updateQuotation(int mPosition, String mQuantity) {
        mUpdatedQuantity = mQuantity;
        mUpdatedPosition = mPosition;
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.UPDATE_QUANTITY);
        map.put("quote_num", mQuotationNumber);
        map.put("product_id", arrayList.get(mPosition).getProduct_id());
        map.put("product_qty", mQuantity);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.UPDATE_QUANTITY, true);
    }

    @Override
    public void deleteQuotation(int mPosition) {
        mUpdatedPosition = mPosition;
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.DELETE_PRODUCT);
        map.put("quote_num", mQuotationNumber);
        map.put("product_id", arrayList.get(mPosition).getProduct_id());
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.DELETE_PRODUCT, true);
    }
}
