package com.nikvay.business_application.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.utils.SuccessDialog;
import com.nikvay.business_application.utils.SuccessDialogClosed;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class LeaveApplicationActivity extends AppCompatActivity implements VolleyCompleteListener, SuccessDialogClosed {

    //shared Preference
   private  SharedUtil sharedUtil;

    private EditText edtSubject,
            edtDescription,
            edtFromDate,
            edtToDate;
    private Button btnSubmitLeave;
    private ImageView iv_back_image_activity;
    private SuccessDialog successDialog;


    //DatePicker
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);

        initialization();
        events();

    }

    private void events() {



        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplicationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                CharSequence strDate = null;
                                Time chosenDate = new Time();
                                chosenDate.set(dayOfMonth, monthOfYear, year);

                                long dateAttendance = chosenDate.toMillis(true);
                                strDate = DateFormat.format("dd-MM-yyyy", dateAttendance);

                                edtFromDate.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        edtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplicationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                CharSequence strDate = null;
                                Time chosenDate = new Time();
                                chosenDate.set(dayOfMonth, monthOfYear, year);

                                long dateAttendance = chosenDate.toMillis(true);
                                strDate = DateFormat.format("dd-MM-yyyy", dateAttendance);

                                edtToDate.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });





        btnSubmitLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String user_id = sharedUtil.getUserDetails().getUser_id();
                 String subject=edtSubject.getText().toString().trim();
                 String description=edtDescription.getText().toString().trim();
                 String fromDate=edtFromDate.getText().toString().trim();
                 String toDate=edtToDate.getText().toString().trim();


                if(subject.equalsIgnoreCase(""))
                {
                    edtSubject.setError("Please Enter Subject");
                    edtSubject.requestFocus();
                }
                else if (description.equalsIgnoreCase(""))
                {
                    edtDescription.setError("Please Enter Description");
                    edtDescription.requestFocus();
                }
                else  if (fromDate.equalsIgnoreCase(""))
                {
                    edtFromDate.setError("Please Select From Date");
                    edtFromDate.requestFocus();
                }
                else if (toDate.equalsIgnoreCase(""))
                {
                    edtToDate.setError("Please Select To Date");
                }
                else
                {
                   callLeaveApplication(subject,description,fromDate,toDate,user_id);
                }

            }
        });
        iv_back_image_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void callLeaveApplication(String subject, String description, String start_date, String toDate, String user_id) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ServerConstants.URL, ServerConstants.serverUrl.LEAVE_APPLICATION);
            map.put("subject", subject);
            map.put("description", description);
            map.put("start_date", start_date);
            map.put("end_date", toDate);
            map.put("sales_person_id", user_id);
            new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.LEAVE_APPLICATION, true);

    }

    private void initialization() {

        sharedUtil = new SharedUtil(this);
        successDialog = new SuccessDialog(this, true);

        iv_back_image_activity=findViewById(R.id.iv_back_image_activity);
        edtSubject=findViewById(R.id.edtSubject);
        edtDescription=findViewById(R.id.edtDescription);
        edtFromDate=findViewById(R.id.edtFromDate);
        edtToDate=findViewById(R.id.edtToDate);
        btnSubmitLeave=findViewById(R.id.btnSubmitLeave);


        edtFromDate.setFocusable(false);
        edtToDate.setFocusable(false);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {
            case ServerConstants.ServiceCode.LEAVE_APPLICATION: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE)) {
                        successDialog.showDialog(" Leave Application Send  Successfully", true);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Leave  Request Not  Successfully", Toast.LENGTH_SHORT).show();

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
