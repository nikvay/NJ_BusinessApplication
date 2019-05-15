package com.nikvay.business_application.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.OutstandingDetailAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.OutstandingDetailModel;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OutstandingDetailActivity extends AppCompatActivity implements VolleyCompleteListener {
    private Toolbar toolbarOD;
    private RecyclerView recyclerOD;
    private String mC_id;
    private OutstandingDetailModel model;
    private ArrayList<OutstandingDetailModel> arrayList = new ArrayList<>();
    private OutstandingDetailAdapter adapter;
    private Button btnSendMail;
    private Dialog dialog;
    private EditText editDialogMail;
    private Button btnDialogSend;
    private TextView textTotalOutstanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding_detail);
        toolbarOD = findViewById(R.id.toolbarOD);
        setSupportActionBar(toolbarOD);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mC_id = getIntent().getExtras().getString(StaticContent.IntentKey.CUSTOMER_ID);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initialize() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_email);
        dialog.setCancelable(true);
        btnDialogSend = dialog.findViewById(R.id.btnDialogSend);
        editDialogMail = dialog.findViewById(R.id.editDialogMail);
        textTotalOutstanding =  findViewById(R.id.textTotalOutstanding);
        textTotalOutstanding.setText("Total Amount: "+getIntent().getExtras().getString(StaticContent.IntentKey.TOTAL_OUTSTANDING));
        btnSendMail = findViewById(R.id.btnSendMail);
        recyclerOD =  findViewById(R.id.recyclerOD);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerOD.setLayoutManager(manager);
        callReceiptWS();
        events();
    }

    private void events() {
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                editDialogMail.setText("");
            }
        });
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });
        btnDialogSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    callSendMailWS();
                }
            }
        });
    }

    private void callSendMailWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.POST_OUTSTANDING_MAIL);
        map.put("customer_id", mC_id);
        if (editDialogMail.getText().toString().length() > 0) {
            map.put("other_email_id", editDialogMail.getText().toString());
        }
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.POST_OUTSTANDING_MAIL, true);
    }

    private boolean isValid() {
        if (editDialogMail.getText().toString().length() > 0) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editDialogMail.getText().toString()).matches()) {
                editDialogMail.setError("Enter Valid Email Id");
                return false;
            } else {
                editDialogMail.setError(null);
            }
        } else {
            editDialogMail.setError(null);
        }
        return true;
    }

    private void callReceiptWS() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.GET_OUTSTANDING_RECEIPT);
        map.put("customer_id", mC_id);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.GET_OUTSTANDING_RECEIPT, true);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.GET_OUTSTANDING_RECEIPT: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("recept_data");
                        arrayList.clear();
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                String amount = jdata.getString("amount");
                                String ref_num = jdata.getString("ref_num");
                                String date = jdata.getString("date");
                                String due_date = jdata.getString("due_date");
                                String overdue_by_days = jdata.getString("overdue_by_days");
                                model = new OutstandingDetailModel();
                                model.setAmount(amount);
                                model.setRef_num(ref_num);
                                model.setDate(date);
                                model.setDue_date(due_date);
                                model.setOverdue_by_days(overdue_by_days);
                                arrayList.add(model);
                            }
                            adapter = new OutstandingDetailAdapter(arrayList, getApplicationContext());
                            recyclerOD.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();

                }
                break;
            }
            case ServerConstants.ServiceCode.POST_OUTSTANDING_MAIL: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {
                        Toast.makeText(getApplicationContext(), "Mail sent", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
}
