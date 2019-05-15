package com.nikvay.business_application.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HolidayListActivity extends AppCompatActivity implements VolleyCompleteListener {

    //shared Preference
    private SharedUtil sharedUtil;
    String user_id,branch_id;

    ImageView iv_back_image_activity;
    Button  btnDownload;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);


        initialization();
        events();
    }

    private void initialization() {
        sharedUtil = new SharedUtil(this);
        user_id = sharedUtil.getUserDetails().getUser_id();
        branch_id = sharedUtil.getUserDetails().getBranch_id();

        iv_back_image_activity=findViewById(R.id.iv_back_image_activity);
        btnDownload=findViewById(R.id.btnDownload);
    }

    private void events() {
        iv_back_image_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callDownloadHolidayList(user_id,branch_id);



            }
        });
    }

    private void callDownloadHolidayList(String user_id, String branch_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.HOLIDAY_LIST);
        map.put("user_id", user_id);
        map.put("branch_id", branch_id);
        new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.HOLIDAY_LIST, true);
    }

    private void downloadHolidayList(String url)
    {
        downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri= Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference=downloadManager.enqueue(request);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {
            case ServerConstants.ServiceCode.HOLIDAY_LIST: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        String folder_path=jsonObject.getString("folder_path");
                        String pdf_name=jsonObject.getString("pdf_name");
                        String url=ServerConstants.serverUrl.BASE_URL+folder_path+pdf_name;
                        downloadHolidayList(url);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Holiday List Not Download", Toast.LENGTH_SHORT).show();

                }
                break;
            }

        }

    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {

    }
}
