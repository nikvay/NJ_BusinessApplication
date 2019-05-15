package com.nikvay.business_application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.common.VibrateOnClick;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QuotationActivity extends AppCompatActivity implements VolleyCompleteListener {

    ImageView iv_back;
    RelativeLayout ll_lost_quo, ll_waiting_for_app, ll_approved, ll_waiting_for_customer, ll_order_rec, ll_ready_app, ll_pi;
    RelativeLayout rr_lost_quo, rr_waiting_for_app, rr_approved, rr_waiting_for_customer, rr_order_rec, rr_ready_app, rr_pi;
    TextView textLostQu_count, text_waiting_for_app_count, text_approved_count, text_waiting_for_customer_count, text_order_rec_count, text_ready_app_count, text_pi_count;
    Intent intent;
    SharedUtil sharedUtil;
    private String user_id;
    String wating_for_approval, approve_by_admin, wating_for_customer, lost, orderRecived, ready_for_approval, pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        find_All_IDs();
        callCountItem();
        VibrateOnClick.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        events();
    }

    private void callCountItem() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.QUOTATION_LIST_COUNT);
        map.put("sales_person_id", user_id);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.QUOTATION_LIST_COUNT, true);

    }

    private void find_All_IDs() {
        sharedUtil = new SharedUtil(this);
        iv_back = findViewById(R.id.iv_back);
        ll_lost_quo = findViewById(R.id.ll_lost_quo);
        ll_waiting_for_app = findViewById(R.id.ll_waiting_for_app);
        ll_approved = findViewById(R.id.ll_approved);
        ll_waiting_for_customer = findViewById(R.id.ll_waiting_for_customer);
        ll_order_rec = findViewById(R.id.ll_order_rec);
        ll_ready_app = findViewById(R.id.ll_ready_app);
        ll_pi = findViewById(R.id.ll_pi);


        rr_lost_quo = findViewById(R.id.rr_lost_quo);
        rr_waiting_for_app = findViewById(R.id.rr_waiting_for_app);
        rr_approved = findViewById(R.id.rr_approved);
        rr_waiting_for_customer = findViewById(R.id.rr_waiting_for_customer);
        rr_order_rec = findViewById(R.id.rr_order_rec);
        rr_ready_app = findViewById(R.id.rr_ready_app);
        rr_pi = findViewById(R.id.rr_pi);

        textLostQu_count = findViewById(R.id.textLostQu_count);
        text_waiting_for_app_count = findViewById(R.id.text_waiting_for_app_count);
        text_approved_count = findViewById(R.id.text_approved_count);
        text_waiting_for_customer_count = findViewById(R.id.text_waiting_for_customer_count);
        text_order_rec_count = findViewById(R.id.text_order_rec_count);
        text_ready_app_count = findViewById(R.id.text_ready_app_count);
        text_pi_count = findViewById(R.id.text_pi_count);


        user_id = sharedUtil.getUserDetails().getUser_id();

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_lost_quo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "0");
                startActivity(intent);
            }
        });

        ll_waiting_for_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "1");
                startActivity(intent);
            }
        });

        ll_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "2");
                startActivity(intent);
            }
        });

        ll_waiting_for_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "3");
                startActivity(intent);
            }
        });

        ll_order_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "4");
                startActivity(intent);
            }
        });

        ll_ready_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "5");
                startActivity(intent);
            }
        });

        ll_pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateOnClick.vibrate();
                intent = new Intent(QuotationActivity.this, QuotationListActivity.class);
                intent.putExtra("STATUS", "6");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.QUOTATION_LIST_COUNT: {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");

                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        if (jsonObject.length() > 0) {


                            wating_for_approval = jsonObject.getString("wating_for_approval");
                            approve_by_admin = jsonObject.getString("approve_by_admin");
                            wating_for_customer = jsonObject.getString("wating_for_customer");
                            lost = jsonObject.getString("lost");
                            orderRecived = jsonObject.getString("orderRecived");
                            ready_for_approval = jsonObject.getString("ready_for_approval");
                            pi = jsonObject.getString("pi");


                            setCount();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }


    }

    private void setCount() {


        if (lost.equals("0")) {
            rr_lost_quo.setBackgroundResource(R.drawable.shape_quotation_count_zero);
        } else {
            rr_lost_quo.setBackgroundResource(R.drawable.shape_quotation_count);
        }
        if (wating_for_approval.equals("0")) {

            rr_waiting_for_app.setBackgroundResource(R.drawable.shape_quotation_count_zero);

        } else {

            rr_waiting_for_app.setBackgroundResource(R.drawable.shape_quotation_count);
        }

        if (approve_by_admin.equals("0")) {
            rr_approved.setBackgroundResource(R.drawable.shape_quotation_count_zero);

        } else {
            rr_approved.setBackgroundResource(R.drawable.shape_quotation_count);

        }
        if (wating_for_customer.equals("0")) {
            rr_waiting_for_customer.setBackgroundResource(R.drawable.shape_quotation_count_zero);

        } else {
            rr_waiting_for_customer.setBackgroundResource(R.drawable.shape_quotation_count);
        }
        if (orderRecived.equals("0")) {
            rr_order_rec.setBackgroundResource(R.drawable.shape_quotation_count_zero);
        } else {
            rr_order_rec.setBackgroundResource(R.drawable.shape_quotation_count);

        }
        if (ready_for_approval.equals("0")) {
            rr_ready_app.setBackgroundResource(R.drawable.shape_quotation_count_zero);
        } else {
            rr_ready_app.setBackgroundResource(R.drawable.shape_quotation_count);

        }
        if (pi.equals("0")) {
            rr_pi.setBackgroundResource(R.drawable.shape_quotation_count_zero);
        } else {
            rr_pi.setBackgroundResource(R.drawable.shape_quotation_count);
        }

        text_order_rec_count.setText(orderRecived);
        text_pi_count.setText(pi);
        text_ready_app_count.setText(ready_for_approval);
        text_approved_count.setText(approve_by_admin);
        text_waiting_for_app_count.setText(wating_for_approval);
        text_waiting_for_customer_count.setText(wating_for_customer);
        textLostQu_count.setText(lost);
    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }
}
