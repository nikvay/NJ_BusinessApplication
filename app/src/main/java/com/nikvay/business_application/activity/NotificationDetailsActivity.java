package com.nikvay.business_application.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikvay.business_application.R;
import com.nikvay.business_application.common.ServerConstants;

public class NotificationDetailsActivity extends AppCompatActivity {

    private String title,file_name,date,file_path;
    private TextView textTitle,textFileName,textDate;
    private ImageView iv_back,iv_downloadNotification;
    DownloadManager downloadManager;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            title=bundle.getString("TITLE");
            file_name=bundle.getString("FILENAME");
            date=bundle.getString("DATE");
            file_path=bundle.getString("FILEPATH");

        }
        find_All_IDs();
        events();
    }

    private void find_All_IDs() {
        iv_back=findViewById(R.id.iv_back);
        textTitle=findViewById(R.id.textTitle);
        textFileName=findViewById(R.id.textFileName);
        textDate=findViewById(R.id.textDate);
        iv_downloadNotification=findViewById(R.id.iv_downloadNotification);


        textTitle.setText(title);
        textFileName.setText(file_name);
        textDate.setText(date);
    }
    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        url=ServerConstants.serverUrl.BASE_URL+file_path+"/"+file_name;
        iv_downloadNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                downloadNotification(url);
            }
        });

    }

    private void downloadNotification(String url) {

        downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri= Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference=downloadManager.enqueue(request);

    }
}
