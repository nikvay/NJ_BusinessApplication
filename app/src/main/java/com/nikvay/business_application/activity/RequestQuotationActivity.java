package com.nikvay.business_application.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.MyCustomerAdapter;
import com.nikvay.business_application.adapter.ProductListAdapter;
import com.nikvay.business_application.adapter.ProductTypeAdapter;
import com.nikvay.business_application.common.ProductTypeNotifier;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.BranchModel;
import com.nikvay.business_application.model.MyCustomerModel;
import com.nikvay.business_application.model.ProductModel;
import com.nikvay.business_application.model.ProductTypeModel;
import com.nikvay.business_application.utils.MathCalculation;
import com.nikvay.business_application.utils.MyCustomerResponse;
import com.nikvay.business_application.utils.SelectedProductInterface;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.utils.SuccessDialogClosed;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.volley_support.MyVolleyGetMethod;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestQuotationActivity extends AppCompatActivity implements VolleyCompleteListener, SelectedProductInterface, ProductTypeNotifier, SuccessDialogClosed {
    private TextView textSelectCustomer,
            textSelectProductQuotation,
            textTotalAmountQoutation,
            textForumQuotation,
            textGrandQuotation;

    private ImageView textSelectCustomerCH,
            textSelectProductQuotationCH;

    private LinearLayout linearCustomerQuotation;
    private AutoCompleteTextView textCompanyNameQuotation,
            textBillingContactPersonQuotation,
            textMobileNumberQuotation,
            textEmailIdQuotation;
    private RecyclerView recyclerProductsQuotation,
            recyclerDialogSC,
            recyclerDialogSP,
            recyclerPT;
    private EditText editSearchC, editSearchP,
            editDiscountQuantity,
            editReference,
            editNote,
            editEmail,editEmailOptiona1,editEmailOptiona2;
    private Spinner spinnerBranchQuotation,
            spinnerGSTQuotation;
    private BranchModel model;
    private ArrayList<BranchModel> arrayList;
    private List<String> branchList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Dialog selectCustomerDialog, selectProductDialog, selectCategoryDialog;
    private Spinner spinnerPT;
    private UserData userData;
    private ArrayList<MyCustomerModel> arrayListC = new ArrayList<>();
    private MyCustomerModel modelC;
    private MyCustomerAdapter adapterC;
    public static MyCustomerModel customerModel = null;
    private Button btnOkDialogSC,
            btnOkDialogSP,
            btnAdd,
            btnCancelDialogSC,
            btnCancelDialogSP,
            btnDiscountQuantity;
    private ProductModel productModel;
    private ArrayList<ProductModel> productArrayList;
    private ArrayList<ProductModel> productArrayListShow = new ArrayList<>();
    private ProductListAdapter productListAdapter, productListAdapterShow;
    private String mBranchID = null;
    private MyCustomerResponse myCustomerResponse;
    private String mTotalAmount = "0", mDiscountLimit = "0", mProductType = null;
    private MathCalculation mathCalculation;
    private LinearLayout linearDiscount;
    private String mDiscountApplied = "0";
    private CardView cardDiscount;
    private ProductTypeModel productTypeModel;
    private ArrayList<ProductTypeModel> arrayListtype = new ArrayList<>();
    private ProductTypeAdapter productTypeAdapter;
    private SuccessDialog successDialog;
    private static final String WITHIN = "within_state", OUTSIDE = "outsite_state", HO = "ho";
    private List<String> productType = new ArrayList<>();
    private ArrayAdapter<String> productCatAdapter;

    private FloatingActionButton selectCustomerDialogFab,selectProductDialogFab, selectCategoryDialogFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_quotation);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        productType.clear();
        productType.add("Local");
        productType.add("Export");
        userData = new UserData(getApplicationContext());
        successDialog = new SuccessDialog(this, true);
        mathCalculation = new MathCalculation();
        myCustomerResponse = new MyCustomerResponse(getApplicationContext());
        selectCategoryDialog = new Dialog(this);
        selectCustomerDialog = new Dialog(this);
        selectProductDialog = new Dialog(this);
        selectCustomerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectProductDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        selectCustomerDialog.setContentView(R.layout.dialog_select_customer);
        selectProductDialog.setContentView(R.layout.dialog_select_product);
        selectCategoryDialog.setContentView(R.layout.dialog_product_type);

        selectCustomerDialog.setCancelable(false);
        selectProductDialog.setCancelable(false);
        selectCategoryDialog.setCancelable(true);
        spinnerPT =  selectCategoryDialog.findViewById(R.id.spinnerPT);
        productCatAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, productType);
        productCatAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerPT.setAdapter(productCatAdapter);
        btnOkDialogSC = selectCustomerDialog.findViewById(R.id.btnOkDialogSC);
        btnCancelDialogSC =  selectCustomerDialog.findViewById(R.id.btnCancelDialogSC);
        editSearchC =  selectCustomerDialog.findViewById(R.id.editSearchC);

        editSearchP = selectProductDialog.findViewById(R.id.editSearchP);
        btnOkDialogSP = selectProductDialog.findViewById(R.id.btnOkDialogSP);
        btnCancelDialogSP = selectProductDialog.findViewById(R.id.btnCancelDialogSP);


        recyclerDialogSC = selectCustomerDialog.findViewById(R.id.recyclerDialogSC);
        recyclerDialogSP =  selectProductDialog.findViewById(R.id.recyclerDialogSP);
        recyclerPT = selectCategoryDialog.findViewById(R.id.recyclerPT);


        selectCategoryDialogFab=selectCategoryDialog.findViewById(R.id.fab);
        selectCustomerDialogFab=selectCustomerDialog.findViewById(R.id.fab);
        selectProductDialogFab=selectProductDialog.findViewById(R.id.fab);


        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);



        recyclerProductsQuotation = findViewById(R.id.recyclerProductsQuotation);
        recyclerProductsQuotation.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager managerSP = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager managerSPS = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager managerPT = new LinearLayoutManager(getApplicationContext());
        recyclerDialogSC.setLayoutManager(manager);
        recyclerDialogSP.setLayoutManager(managerSP);
        recyclerProductsQuotation.setLayoutManager(managerSPS);
        recyclerPT.setLayoutManager(managerPT);
        textCompanyNameQuotation =  findViewById(R.id.textCompanyNameQuotation);
        textBillingContactPersonQuotation =  findViewById(R.id.textBillingContactPersonQuotation);
        textMobileNumberQuotation = findViewById(R.id.textMobileNumberQuotation);
        textEmailIdQuotation =  findViewById(R.id.textEmailIdQuotation);
        btnAdd = findViewById(R.id.btnAdd);
        textCompanyNameQuotation.setEnabled(false);
        textBillingContactPersonQuotation.setEnabled(false);
        textMobileNumberQuotation.setEnabled(false);
        textEmailIdQuotation.setEnabled(false);
        linearCustomerQuotation =  findViewById(R.id.linearCustomerQuotation);
        textSelectCustomer = findViewById(R.id.textSelectCustomer);
        textSelectProductQuotation =  findViewById(R.id.textSelectProductQuotation);
        textTotalAmountQoutation = findViewById(R.id.textTotalAmountQoutation);
        textForumQuotation = findViewById(R.id.textForumQuotation);
        textGrandQuotation = findViewById(R.id.textGrandQuotation);
        textSelectCustomerCH =  findViewById(R.id.textSelectCustomerCH);
        textSelectProductQuotationCH =findViewById(R.id.textSelectProductQuotationCH);
        linearDiscount = findViewById(R.id.linearDiscount);
        editDiscountQuantity = findViewById(R.id.editDiscountQuantity);
        editNote =  findViewById(R.id.editNote);
        editReference = findViewById(R.id.editReference);
        editEmail = findViewById(R.id.editEmail);
        //  editDiscountQuantity.setKeyListener(new CustomDigitsKeyListener(true, true));
        btnDiscountQuantity =  findViewById(R.id.btnDiscountQuantity);
        cardDiscount =  findViewById(R.id.cardDiscount);

        spinnerBranchQuotation =  findViewById(R.id.spinnerBranchQuotation);
        spinnerGSTQuotation =  findViewById(R.id.spinnerGSTQuotation);
        editEmailOptiona1 =  findViewById(R.id.editEmailOptiona1);
        editEmailOptiona2 =  findViewById(R.id.editEmailOptiona2);




        events();
    }

    private void events() {
        callBranchWS();
        textSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomerWS();

            }
        });
        textSelectCustomerCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                callCustomerWS();

            }
        });
        btnOkDialogSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                editSearchC.setText("");
                selectCustomerDialog.dismiss();
                if (customerModel != null) {
                    editDiscountQuantity.setHint("0 " + " to " + customerModel.getDiscount());
                    setCustomerData();
                    mDiscountLimit = customerModel.getDiscount();
                    if (productArrayListShow.size() > 0) {
                        editDiscountQuantity.setText("");
                        setDiscountData(mTotalAmount, mDiscountLimit, true);
                    }
                    linearCustomerQuotation.setVisibility(View.VISIBLE);
                } else {
                    clearDiscountData();
                    linearCustomerQuotation.setVisibility(View.GONE);
                }
            }
        });

        textSelectProductQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // callProductListWS();
                callProductTypeWS();
            }
        });
        textSelectProductQuotationCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // callProductListWS();
                VibrateOnClick.vibrate();
                callProductTypeWS();
            }
        });
        btnOkDialogSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                editSearchP.setText("");
                selectProductDialog.dismiss();
            }
        });

        btnCancelDialogSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                customerModel = null;
                editDiscountQuantity.setText("");
                clearDiscountData();
                linearCustomerQuotation.setVisibility(View.GONE);
                editSearchC.setText("");
                selectCustomerDialog.dismiss();
            }
        });
        btnCancelDialogSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                if (productArrayListShow.size() > 0) {
                    productArrayListShow.clear();
                    productListAdapterShow.notifyDataSetChanged();

                }
                editSearchP.setText("");
                selectProductDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                if (isValid()) {
                    callApplyQuoatationWS();
                }
            }
        });

        spinnerBranchQuotation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mBranchID = arrayList.get(position - 1).getBranch_id();
                } else {
                    mBranchID = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        editSearchP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productListAdapter.getFilter().filter(editSearchP.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnDiscountQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = editDiscountQuantity.getText().toString();
                if (check.length() != 0) {
                    int g = 0;
                    if (check.length() == 1) {
                        if (check.equals(".")) {
                            editDiscountQuantity.setText(mDiscountLimit);
                        }
                    }

                    for (int i = 0; i < check.length(); i++) {
                        if (check.charAt(i) == '.') {
                            g++;
                            if (g > 1) {
                                editDiscountQuantity.setText(mDiscountLimit);
                                break;
                            }
                        }
                    }
                } else {
                    editDiscountQuantity.setText(mDiscountLimit);
                }


                float discount = Float.valueOf(editDiscountQuantity.getText().toString());
                if (discount <= Float.valueOf(mDiscountLimit)) {
                    setDiscountData(mTotalAmount, String.valueOf(discount), true);
                    mDiscountApplied = editDiscountQuantity.getText().toString();
                    successDialog.showDialog("Discount Applied", false);
                } else {
                    editDiscountQuantity.setError("0 to " + mDiscountLimit + " only");
                }

            }
        });

        spinnerPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                callProductTypeWS();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        recyclerDialogSC.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1))
                {
                    selectCustomerDialogFab.hide();
                }
                else if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    selectCustomerDialogFab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                if(dy==0)
                {
                    selectCustomerDialogFab.show();

                }
                else if(dy>0){

                    selectCustomerDialogFab.show();
                }
                else if(dy<0)
                {
                    selectCustomerDialogFab.hide();
                }



                super.onScrolled(recyclerView,dx,dy);



            }
        });



        recyclerDialogSP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1))
                {
                    selectProductDialogFab.hide();
                }
                else if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    selectProductDialogFab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                if(dy==0)
                {
                    selectProductDialogFab.show();

                }
                else if(dy>0){

                    selectProductDialogFab.show();
                }
                else if(dy<0)
                {
                    selectProductDialogFab.hide();
                }



                super.onScrolled(recyclerView,dx,dy);



            }
        });



        recyclerPT.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1))
                {
                    selectCategoryDialogFab.hide();
                }
                else if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    selectCategoryDialogFab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                if(dy==0)
                {
                    selectCategoryDialogFab.show();

                }
                else if(dy>0){

                    selectCategoryDialogFab.show();
                }
                else if(dy<0)
                {
                    selectCategoryDialogFab.hide();
                }



                super.onScrolled(recyclerView,dx,dy);



            }
        });



    }

    private void callProductTypeWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.POST_PRODUCT_TYPE);
        map.put("local_or_export", productType.get(spinnerPT.getSelectedItemPosition() < 0 ? 0 : spinnerPT.getSelectedItemPosition()));
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.POST_PRODUCT_TYPE, true);
    }

    private void clearDiscountData() {
        cardDiscount.setVisibility(View.GONE);
        editDiscountQuantity.setText("");
        textTotalAmountQoutation.setText("");
        textForumQuotation.setText("");
        textGrandQuotation.setText("");
    }


    private boolean isValid() {
        if (customerModel == null) {
            Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBranchID == null) {
            Toast.makeText(getApplicationContext(), "Select Branch", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerGSTQuotation.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Select GST type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (productArrayListShow.size() == 0 || productArrayListShow.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Select Product", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editEmail.getText().toString().length() > 0) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()) {
                Toast.makeText(getApplicationContext(), "Enter valid email id", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void callApplyQuoatationWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.ADD_QUOTATION);
        map.put("product_id", String.valueOf(getProductArray()));
        map.put("sales_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        map.put("customer_id", customerModel.getC_id());
        map.put("branch_id", mBranchID);
        map.put("product_price", String.valueOf(getProductPriceArray()));
        map.put("product_discounted_price", textGrandQuotation.getText().toString());
        // map.put("discount_value", mDiscountApplied.equals("0") ? mDiscountLimit : mDiscountApplied);
        map.put("discount_value", mDiscountApplied);
        map.put("qty", String.valueOf(getDVProductQuantityArray()));
        map.put("local_or_export", spinnerPT.getSelectedItem().toString());
        map.put("product_type", mProductType);
        if (!editNote.getText().toString().isEmpty()) {
            map.put("note", editNote.getText().toString());
        }
        switch (spinnerGSTQuotation.getSelectedItemPosition()) {
            case 1: {
                map.put("gst_type", WITHIN);

                break;
            }
            case 2: {
                map.put("gst_type", OUTSIDE);

                break;
            }
            case 3: {
                map.put("gst_type", HO);

            }
        }
        map.put("additional_text", String.valueOf(getDescriptionArray()));
        if (editReference.getText().toString().length() > 0) {
            map.put("reference", editReference.getText().toString());
        }
        map.put("stock_status", String.valueOf(getProductStockStatus()));
        if (editEmail.getText().toString().length() > 0) {
            map.put("cc_email", editEmail.getText().toString());

        }
        if (editEmailOptiona1.getText().toString().length() > 0) {
            map.put("cc_email1", editEmailOptiona1.getText().toString());

        }
        if (editEmailOptiona2.getText().toString().length() > 0) {
            map.put("cc_email2", editEmailOptiona2.getText().toString());
        }

        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.ADD_QUOTATION, true);
    }

    private JSONArray getDVProductQuantityArray() {
        List<String> dvppList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            dvppList.add(productArrayListShow.get(i).getQuantity());
        }
        JSONArray dvppJsonArray = new JSONArray(dvppList);
        return dvppJsonArray;
    }

    private JSONArray getProductStockStatus() {
        List<String> dvppList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            dvppList.add(productArrayListShow.get(i).getStock_status());
        }
        JSONArray dvppJsonArray = new JSONArray(dvppList);
        return dvppJsonArray;
    }

    private JSONArray getDescriptionArray() {
        List<String> dvppList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            dvppList.add(productArrayListShow.get(i).getProduct_description());
        }
        JSONArray dvppJsonArray = new JSONArray(dvppList);
        return dvppJsonArray;
    }

    private JSONArray getDVProductPriceArray() {
        List<String> dvppList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            dvppList.add(productArrayListShow.get(i).getDiscountNumber());
        }
        JSONArray dvppJsonArray = new JSONArray(dvppList);
        return dvppJsonArray;
    }

    private JSONArray getDProductPriceArray() {
        List<String> dppList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            dppList.add(productArrayListShow.get(i).getPriceAfterDiscount());
        }
        JSONArray dppJsonArray = new JSONArray(dppList);
        return dppJsonArray;
    }

    private JSONArray getProductPriceArray() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            ppList.add(productArrayListShow.get(i).getPrice());
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }


    private JSONArray getProductArray() {
        List<String> pList = new ArrayList<>();
        for (int i = 0; i < productArrayListShow.size(); i++) {
            pList.add(productArrayListShow.get(i).getProduct_id());
        }
        JSONArray pJsonArray = new JSONArray(pList);
        return pJsonArray;
    }

       private void callProductListWS(String mID) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.GET_PRODUCT);
        map.put("product_type", mID);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.GET_PRODUCT, true);
    }

    private void setCustomerData() {
        textCompanyNameQuotation.setText(customerModel.getCompany_name());
        textBillingContactPersonQuotation.setText(customerModel.getBilling_contact_person());
        textMobileNumberQuotation.setText(customerModel.getCell_no());
        textEmailIdQuotation.setText(customerModel.getEmail_id());

    }

    private void callCustomerWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.MY_CUSTOMER_LIST);
        map.put("sale_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.MY_CUSTOMER_LIST, true);
    }

    private void callBranchWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.BRANCH_LIST);
        new MyVolleyGetMethod(this, map, ServerConstants.ServiceCode.BRANCH_LIST, true);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.BRANCH_LIST: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("branch_details");
                        if (jsonArray.length() > 0) {
                            arrayList = new ArrayList<>();
                            arrayList.clear();
                            branchList.clear();
                            branchList.add("Select Branch");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String branch_id = jdata.getString("branch_id");
                                String name = jdata.getString("name");
                                String status = jdata.getString("status");
                                model = new BranchModel();
                                model.setBranch_id(branch_id);
                                model.setName(name);
                                model.setStatus(status);
                                arrayList.add(model);
                                branchList.add(name);
                            }
                            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branchList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerBranchQuotation.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "Branch list not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Branch list not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Branch list not found", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case ServerConstants.ServiceCode.MY_CUSTOMER_LIST: {
                if (arrayListC != null) {
                    arrayListC.clear();
                }
                arrayListC = myCustomerResponse.getCustomerResponse(response);
                if (arrayListC != null) {
                    selectCustomerDialogFab.show();
                    adapterC = new MyCustomerAdapter(getApplicationContext(), arrayListC, true);
                    selectCustomerDialog.show();
                    Window window = selectCustomerDialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    recyclerDialogSC.setAdapter(adapterC);
                }
                break;
            }
            case ServerConstants.ServiceCode.GET_PRODUCT: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    String discount_limit = jsonObject.getString("discount_limit");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("product_list");
                        if (jsonArray.length() > 0) {
                            selectProductDialogFab.show();
                            productArrayList = new ArrayList<>();
                            productArrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String product_id = jdata.getString("product_id");
                                String name = jdata.getString("name");
                                String price = jdata.getString("price");
                                String gross_wt = jdata.getString("net_wt");
                                String net_wt = jdata.getString("net_wt");
                                String dimension = jdata.getString("dimension");
                                String stock_count = jdata.getString("stock_count");
                                String accessories = jdata.optString("accessories");
                                productModel = new ProductModel();
                                productModel.setName(name);
                                productModel.setPrice(price.replace(",", ""));
                                productModel.setProduct_id(product_id);
                                productModel.setGross_wt(gross_wt + " kg");
                                productModel.setNet_wt(net_wt + " kg");
                                productModel.setDiscountNumber("0");
                                productModel.setPriceAfterDiscount("0");
                                productModel.setDimension(dimension);
                                productModel.setQuantity("1");
                                productModel.setStock_count(stock_count.equals("null") ? "0" : stock_count);
                                productModel.setStock_status(StaticContent.StockStatus.EX_STOCK);
                                productModel.setDiscount_limit(discount_limit);
                                productModel.setAccessories(accessories.equals("null") ? " " : accessories);
                                productModel.setSelected(false);
                                productArrayList.add(productModel);
                            }
                            productListAdapter = new ProductListAdapter(getApplicationContext(), productArrayList, this, true);
                            selectProductDialog.show();
                            productArrayListShow.clear();
                            Window window = selectProductDialog.getWindow();
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            recyclerDialogSP.setAdapter(productListAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_SHORT).show();
                            callProductTypeWS();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_SHORT).show();
                        callProductTypeWS();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case ServerConstants.ServiceCode.ADD_QUOTATION: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        // Toast.makeText(getApplicationContext(), "Quotation applied successfully", Toast.LENGTH_SHORT).show();
                        successDialog.showDialog("Quotation created successfully", true);
                        customerModel = null;

                    } else {
                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case ServerConstants.ServiceCode.POST_PRODUCT_TYPE: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            selectCategoryDialogFab.show();
                            arrayListtype.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String id = jdata.getString("id");
                                String type_name = jdata.getString("type_name");
                                productTypeModel = new ProductTypeModel();
                                productTypeModel.setId(id);
                                productTypeModel.setType_name(type_name);
                                arrayListtype.add(productTypeModel);
                            }
                            selectCategoryDialog.show();
                            Window window = selectCategoryDialog.getWindow();
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            productTypeAdapter = new ProductTypeAdapter(arrayListtype, this);
                            recyclerPT.setAdapter(productTypeAdapter);
                        } else {

                            if (arrayListtype.size() > 0) {
                                selectCategoryDialogFab.hide();
                                arrayListtype.clear();
                                productTypeAdapter.notifyDataSetChanged();
                            }
                            Toast.makeText(getApplicationContext(), "Coming soon...", Toast.LENGTH_SHORT).show();

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }
    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {
        if (productArrayListShow.size() != 0 && customerModel != null) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            customerModel = null;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customerModel = null;
        finish();
    }

    @Override
    public void addSelectedProduct(ProductModel productModel) {
        editDiscountQuantity.setText("");
        productArrayListShow.add(productModel);
        if (productArrayListShow.size() != 0) {
            productListAdapterShow = new ProductListAdapter(this, productArrayListShow, this, false);
            recyclerProductsQuotation.setAdapter(productListAdapterShow);
            recyclerProductsQuotation.setVisibility(View.VISIBLE);
            mTotalAmount = mathCalculation.add(mTotalAmount, productModel.getPrice());
            setDiscountData(mTotalAmount, mDiscountLimit, true);
        } else {
            productListAdapter.notifyDataSetChanged();
        }
    }

    private void setDiscountData(String mTotalAmount, String mDiscount, boolean toShow) {
       /* editDiscountQuantity.setText(mDiscount);
        editDiscountQuantity.setHint(" 0 " + " to " + mDiscount);*/
        if (customerModel != null) {
            int sum = 0;
            if (productArrayListShow.size() > 0) {

                for (int i = 0; i < productArrayListShow.size(); i++) {
                    sum = sum + Integer.valueOf(productArrayListShow.get(i).getPrice()) * Integer.valueOf(productArrayListShow.get(i).getQuantity());
                }
            }
            cardDiscount.setVisibility(View.VISIBLE);
            textTotalAmountQoutation.setText("Total: " + sum);
            textTotalAmountQoutation.setText("Total: " + sum);
            textForumQuotation.setText(sum + " - " + mDiscount + " % ");
            textGrandQuotation.setText(String.format("%.0f", mathCalculation.calculatePer(mDiscount, String.valueOf(sum))));
            if (productArrayListShow.size() < 0) {
                clearDiscountData();
                cardDiscount.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void subSelectedProduct(ProductModel productModel) {
        editDiscountQuantity.setText("");
        if (productArrayListShow.size() > 0) {
            mTotalAmount = mathCalculation.sub(mTotalAmount, productModel.getPrice());
            setDiscountData(mTotalAmount, mDiscountLimit, true);
            for (int i = 0; i < productArrayListShow.size(); i++) {
                if (productModel.getProduct_id().equals(productArrayListShow.get(i).getProduct_id())) {
                    productArrayListShow.remove(i);
/*
                    if(productArrayListShow.size()==0){
                        textSelectProductQuotation.setText("Select Products");
                        textSelectProductQuotation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_icon, 0);
                        recyclerProductsQuotation.setVisibility(View.GONE);
                    }
*/

                }
            }
            productListAdapterShow.notifyDataSetChanged();

        } else {
            clearDiscountData();
        }

    }

    @Override
    public void quantityNotify() {
        if (productArrayListShow.size() > 0 && customerModel != null) {
            setDiscountData(mTotalAmount, mDiscountLimit, true);
        } else {
            editDiscountQuantity.setText("");
            clearDiscountData();
        }
    }

    @Override
    public void removeSelectedProduct() {
        editDiscountQuantity.setText("");
        setDiscountData(mTotalAmount, mDiscountLimit, true);
        if (productArrayListShow.size() <= 0) {
            cardDiscount.setVisibility(View.GONE);
        }
    }

    @Override
    public void selectedProductType(String mID) {
        mProductType = mID;
        callProductListWS(mID);
        selectCategoryDialog.dismiss();
    }

    @Override
    public void dialogClosed(boolean mClosed) {
        startActivity(new Intent(RequestQuotationActivity.this, QuotationListActivity.class).putExtra("STATUS", "5"));
        finish();
    }
}
