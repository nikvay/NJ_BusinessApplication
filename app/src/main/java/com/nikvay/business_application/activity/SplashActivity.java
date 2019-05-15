package com.nikvay.business_application.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.nikvay.business_application.R;
import com.nikvay.business_application.adapter.ViewCollectionAdapter;
import com.nikvay.business_application.common.RuntimePermissionsActivity;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.CNP;
import com.nikvay.business_application.model.CollectionModel;
import com.nikvay.business_application.utils.Logs;
import com.nikvay.business_application.utils.ResponseUtil;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.StaticContent;
import com.nikvay.business_application.volley_support.MyVolleyPostMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Tamboli on 16-Sep-17.
 */

public class SplashActivity extends RuntimePermissionsActivity  implements VolleyCompleteListener {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 20;
    SharedUtil sharedUtil;
    TextView text_cnp_message,text_application_name;
    ImageView iv_logo;
    int duration = 3000;
    Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);




        text_cnp_message = findViewById(R.id.text_cnp_message);
        text_application_name = findViewById(R.id.text_application_name);
        setScaleAnimation(text_cnp_message);


         callSplashScrrenWS();

        sharedUtil = new SharedUtil(SplashActivity.this);

        iv_logo = findViewById(R.id.iv_logo);
        setScaleAnimationImage(iv_logo);

      /*  final RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(850); //Put desired duration per anim cycle here, in milliseconds

        iv_logo.startAnimation(anim);//Start animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_logo.setAnimation(null);  //Later on, use view.setAnimation(null) to stop it.
            }
        }, 1000);*/


        if (isDeviceBuildVersionMarshmallow()) {
            getPermisson();
        } else {
            handler(duration);
        }
    }

    private void callSplashScrrenWS() {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(ServerConstants.URL, ServerConstants.serverUrl.APPLICATION_DATA);
                new MyVolleyPostMethod(SplashActivity.this, map, ServerConstants.ServiceCode.APPLICATION_DATA, true);

    }

    private boolean isDeviceBuildVersionMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void getPermisson() {
        SplashActivity.super.requestAppPermissions(new
                        String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isDeviceBuildVersionMarshmallow()) {
            getPermisson();
        } else {
            handler(duration);
        }
    }

    private void handler(final int duration) {
        handler.postDelayed(mUpdateTimeTask, duration);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {

                if (sharedUtil.getUserDetails().getUser_id() == null) {
                    Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onPermissionsGranted(final int requestCode) {
        if (requestCode == REQUEST_PERMISSIONS) {
            handler(duration);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.REVERSE, 0.5f, Animation.REVERSE, 0.5f);
        anim.setDuration(300);
        view.animate();
        view.startAnimation(anim);
    }

    private void setScaleAnimationImage(View view) {
        ScaleAnimation anim = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.REVERSE, 0.5f, Animation.REVERSE, 0.5f);
        anim.setDuration(300);
        view.animate();
        view.startAnimation(anim);
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.APPLICATION_DATA:

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    String img_path = jsonObject.getString("img_path");
                    String name = "",image_urlSplash = "",full_imageUrlSplash,image_urlScreen = "",full_imageScreen,message="";
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("flashscreenimage");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jdata = jsonArray.getJSONObject(i);
                                 name = jdata.getString("name");
                                 image_urlSplash = jdata.getString("flash_screen_image");
                                 image_urlScreen = jdata.getString("image_url");
                                 message = jdata.getString("massage");
                            }
                            full_imageUrlSplash=ServerConstants.serverUrl.BASE_URL+img_path+"/"+image_urlSplash;
                            full_imageScreen=ServerConstants.serverUrl.BASE_URL+img_path+"/"+image_urlScreen;
                            sharedUtil.addapplicationNameImageMessage(full_imageUrlSplash,full_imageScreen,name,message);


                            Glide.with(SplashActivity.this).load(full_imageUrlSplash).into(iv_logo);
                            text_application_name.setText(name);
                            text_cnp_message.setText(message);



                        } else {

                            Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                }


                break;
        }

    }

    @Override
    public void onTaskFailed(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.APPLICATION_DATA:
                Logs.showLogE(TAG, response);
                break;
        }
    }




}