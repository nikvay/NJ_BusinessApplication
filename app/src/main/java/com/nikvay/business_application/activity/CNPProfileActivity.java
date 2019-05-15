package com.nikvay.business_application.activity;

import android.app.Dialog;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.CNPProfileAdapter;
import com.nikvay.business_application.adapter.CNPProfileSubTypeListAdapter;
import com.nikvay.business_application.adapter.MyCustomerAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.model.CNPProfileSubTypeListModel;
import com.nikvay.business_application.model.ExplodedViewModel;
import com.nikvay.business_application.model.MyCustomerModel;
import com.nikvay.business_application.utils.MyCustomerResponse;
import com.nikvay.business_application.utils.SelectCNPProfileInterface;
import com.nikvay.business_application.utils.SelectCNPProjectSubType;
import com.nikvay.business_application.utils.SelectCustomerInterface;
import com.nikvay.business_application.utils.SelectExplodedView;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.utils.SuccessDialogClosed;
import com.nikvay.business_application.utils.UserData;
import com.nikvay.business_application.utils.ValidationUtil;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CNPProfileActivity extends AppCompatActivity implements VolleyCompleteListener, SelectCNPProfileInterface, SuccessDialogClosed, SelectCNPProjectSubType, SelectCustomerInterface {


    ImageView iv_back_image_activity, iv_addEmailId;

    //Shared preference
    SharedUtil sharedUtil;

    EditText edtCustomerEmailId, edtOptionalEmailId;
    public static boolean isToRefresh = false;
    Button btnSend;

    ImageView textCNPProfileCH;
    String user_id;
    ExplodedViewModel explodedViewModel;
    TextView textCNPProfile;

    ArrayList<ExplodedViewModel> explodedViewModelsArrayKList;
    ArrayList<CNPProfileSubTypeListModel> cnpProfileSubTypeListModelArrayList;


    //=======Dialog  code====
    Dialog selectExplodedPdfDialog, subSelectPdfDialog;
    RecyclerView recyclerDialogEV, recyclerSubDialogEV;
    CNPProfileAdapter cnpProfileAdapter;
    CNPProfileSubTypeListAdapter cnpProfileSubTypeListAdapter;
    String pdfName, subPdfName, pdfType;


    private FloatingActionButton fab, fabSubtype;
    String selectedId;
    String profileSubId = "";
    private SuccessDialog successDialog;
    SelectExplodedView selectExplodedView;
    String emailId, optionalEmailId;


    //  select Customer Dialog
    private TextView tv_select_customer_profile;
    private Dialog selectCustomerDialog;
    private Button btnOkDialogSC, btnCancelDialogSC;
    private EditText editSearchC;
    private RecyclerView recyclerDialogSC;
    private UserData userData;
    private ArrayList<MyCustomerModel> arrayListC = new ArrayList<>();
    private MyCustomerResponse myCustomerResponse;
    private MyCustomerAdapter adapterC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnpprofile);
        initialize();
    }

    private void initialize() {
        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sharedUtil = new SharedUtil(this);
        userData = new UserData(getApplicationContext());
        successDialog = new SuccessDialog(this, true);
        iv_back_image_activity = findViewById(R.id.iv_back_image_activity);
        edtCustomerEmailId = findViewById(R.id.edtCustomerEmailId);
        btnSend = findViewById(R.id.btnSend);
        textCNPProfileCH = findViewById(R.id.textCNPProfileCH);
        iv_addEmailId = findViewById(R.id.iv_addEmailId);
        edtOptionalEmailId = findViewById(R.id.edtOptionalEmailId);
        textCNPProfile = findViewById(R.id.textCNPProfile);


        //Select First pdf Dialog

        selectExplodedPdfDialog = new Dialog(this);
        selectExplodedPdfDialog.setContentView(R.layout.dialog_explodedview_pdf);

        recyclerDialogEV = selectExplodedPdfDialog.findViewById(R.id.recyclerViewExplodedPdf);


        fab = selectExplodedPdfDialog.findViewById(R.id.fab);



        selectExplodedPdfDialog.setCancelable(true);

        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        recyclerDialogEV.setLayoutManager(layout);
        recyclerDialogEV.setHasFixedSize(true);


        //Select Second pdf Dialog

        subSelectPdfDialog = new Dialog(this);
        subSelectPdfDialog.setContentView(R.layout.dialog_explodedview_pdf);

        recyclerSubDialogEV = subSelectPdfDialog.findViewById(R.id.recyclerViewExplodedPdf);
        fabSubtype = subSelectPdfDialog.findViewById(R.id.fab);


        subSelectPdfDialog.setCancelable(true);

        LinearLayoutManager layoutSubSelected = new LinearLayoutManager(getApplicationContext());
        recyclerSubDialogEV.setLayoutManager(layoutSubSelected);
        recyclerSubDialogEV.setHasFixedSize(true);



        myCustomerResponse = new MyCustomerResponse(getApplicationContext());


        tv_select_customer_profile = findViewById(R.id.tv_select_customer_profile);

        selectCustomerDialog = new Dialog(this);
        selectCustomerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectCustomerDialog.setContentView(R.layout.dialog_select_customer);
        selectCustomerDialog.setCancelable(false);
        btnOkDialogSC = selectCustomerDialog.findViewById(R.id.btnOkDialogSC);
        btnCancelDialogSC = selectCustomerDialog.findViewById(R.id.btnCancelDialogSC);
        editSearchC = selectCustomerDialog.findViewById(R.id.editSearchC);
        recyclerDialogSC = selectCustomerDialog.findViewById(R.id.recyclerDialogSC);
        recyclerDialogSC = selectCustomerDialog.findViewById(R.id.recyclerDialogSC);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerDialogSC.setLayoutManager(manager);


        events();
    }

    private void events() {

        iv_back_image_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textCNPProfileCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateOnClick.vibrate();
                callProfileTypeWS();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateOnClick.vibrate();
                emailId = edtCustomerEmailId.getText().toString().trim();
                optionalEmailId = edtOptionalEmailId.getText().toString().trim();

                if (emailId.equalsIgnoreCase("")) {
                    edtCustomerEmailId.setError("Please Enter Email Id");
                    edtCustomerEmailId.requestFocus();
                } else if (!ValidationUtil.emailCheck(emailId)) {
                    edtCustomerEmailId.setError("Invalid EmailId");
                    edtCustomerEmailId.requestFocus();

                } else if (selectedId == null) {
                    Toast.makeText(CNPProfileActivity.this, "Please Select Exploded Pdf", Toast.LENGTH_SHORT).show();
                } else {
                    if (optionalEmailId.equalsIgnoreCase("")) {
                        callProfileSendWS(selectedId, emailId);
                    } else if (!ValidationUtil.emailCheck(optionalEmailId)) {
                        edtOptionalEmailId.setError("Invalid EmailId");
                        edtOptionalEmailId.requestFocus();
                    } else {
                        String emailID = emailId + "," + optionalEmailId;
                        callProfileSendWS(selectedId, emailID);
                    }
                }


            }
        });

        iv_addEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtOptionalEmailId.setVisibility(View.VISIBLE);
                iv_addEmailId.setVisibility(View.GONE);

            }
        });


        recyclerDialogEV.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        recyclerSubDialogEV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    fabSubtype.hide();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fabSubtype.show();
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                if (dy == 0) {
                    fabSubtype.show();

                } else if (dy > 0) {

                    fabSubtype.show();
                } else if (dy < 0) {
                    fabSubtype.hide();
                }


                super.onScrolled(recyclerView, dx, dy);


            }
        });
        tv_select_customer_profile.setOnClickListener(new View.OnClickListener() {
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
        btnCancelDialogSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCustomerEmailId.setText("");
                selectCustomerDialog.dismiss();
            }
        });
        btnOkDialogSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCustomerDialog.dismiss();
            }
        });

    }

    private void callProfileSendWS(String selectedId, String emailId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.SEND_CNP_PROFILE);
        map.put("pdf_id", selectedId);
        map.put("sub_type_id", profileSubId);
        map.put("email_id", emailId);
        map.put("user_id", user_id);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.SEND_CNP_PROFILE, true);
    }

    private void callProfileTypeWS() {
        user_id = sharedUtil.getUserDetails().getUser_id();
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.CNP_PROFILE);
        map.put("user_id", user_id);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.CNP_PROFILE, true);
    }

    private void callMyCustomerWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.MY_CUSTOMER_LIST);
        map.put("sale_person_id", userData.getUserData(StaticContent.UserData.USER_ID));
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.MY_CUSTOMER_LIST, true);
    }

    private void callSubProfileTypeWS(String selectedId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.CNP_SUBPROFILE);
        map.put("profile_id", selectedId);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.CNP_SUBPROFILE, true);
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {
            case ServerConstants.ServiceCode.CNP_PROFILE: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            fab.show();
                            explodedViewModelsArrayKList = new ArrayList<>();
                            explodedViewModelsArrayKList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String id = jdata.getString("id");
                                String name = jdata.getString("name");
                                String pdf_name = jdata.getString("pdf_name");

                                ExplodedViewModel explodedViewModel = new ExplodedViewModel();
                                explodedViewModel.setId(id);
                                explodedViewModel.setPdfName(name);
                                explodedViewModel.setName(pdf_name);
                                explodedViewModelsArrayKList.add(explodedViewModel);
                            }

                            cnpProfileAdapter = new CNPProfileAdapter(this, explodedViewModelsArrayKList, true);
                            selectExplodedPdfDialog.show();
                            Window window = selectExplodedPdfDialog.getWindow();
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            recyclerDialogEV.setAdapter(cnpProfileAdapter);

                        }

                    } else {
                        if (explodedViewModelsArrayKList.size() > 0) {
                            explodedViewModelsArrayKList.clear();
                            cnpProfileAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();

                }
                break;
            }
            case ServerConstants.ServiceCode.CNP_SUBPROFILE: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("sub_type");
                        if (jsonArray.length() > 0) {
                            fabSubtype.show();
                            cnpProfileSubTypeListModelArrayList = new ArrayList<>();
                            cnpProfileSubTypeListModelArrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String id = jdata.getString("id");
                                String name = jdata.getString("subtype_name");

                                CNPProfileSubTypeListModel cnpProfileSubTypeListModel = new CNPProfileSubTypeListModel();
                                cnpProfileSubTypeListModel.setId(id);
                                cnpProfileSubTypeListModel.setName(name);

                                cnpProfileSubTypeListModelArrayList.add(cnpProfileSubTypeListModel);
                            }

                            cnpProfileSubTypeListAdapter = new CNPProfileSubTypeListAdapter(this, cnpProfileSubTypeListModelArrayList, true);
                            subSelectPdfDialog.show();
                            Window window = subSelectPdfDialog.getWindow();
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            recyclerSubDialogEV.setAdapter(cnpProfileSubTypeListAdapter);

                        }

                        selectExplodedPdfDialog.dismiss();

                    } else {
                        if (explodedViewModelsArrayKList.size() > 0) {
                            explodedViewModelsArrayKList.clear();
                            cnpProfileAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();

                }
                break;
            }

            case ServerConstants.ServiceCode.SEND_CNP_PROFILE: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE)) {

                        successDialog.showDialog("Profile Send Successfully", true);
                        selectedId = null;
                        edtCustomerEmailId.setText("");
                        edtOptionalEmailId.setText("");
                        edtOptionalEmailId.setVisibility(View.GONE);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), " Not Send Try Again", Toast.LENGTH_SHORT).show();

                }
                break;
            }

            case ServerConstants.ServiceCode.MY_CUSTOMER_LIST: {
                arrayListC.clear();
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
    public void getExplodedId(String explodedViewId) {
        this.selectedId = explodedViewId;
        selectExplodedPdfDialog.dismiss();
        callSubProfileTypeWS(this.selectedId);


    }

    @Override
    public void getPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    @Override
    public void getPdfType(String pdfType) {
        this.pdfType = pdfType;
        if (pdfType.equalsIgnoreCase("null")) {
            this.pdfName = null;
        } else {
            textCNPProfile.setText(this.pdfName);
        }

    }

    @Override
    public void dialogClosed(boolean mClosed) {
        ExplodedViewActivity.isToRefresh = true;
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getProfileSubtype(String profileSubId) {
        this.profileSubId = profileSubId;
        subSelectPdfDialog.dismiss();
    }

    @Override
    public void getSubPdfName(String subPdfName) {
        this.subPdfName = subPdfName;

        textCNPProfile.setText("");
        textCNPProfile.setText(this.subPdfName);
    }

    @Override
    public void getCustomerName(String mCustomerName) {

    }

    @Override
    public void getCustomerDetail(MyCustomerModel customerModel) {
        edtCustomerEmailId.setText(customerModel.getEmail_id());

    }
}
