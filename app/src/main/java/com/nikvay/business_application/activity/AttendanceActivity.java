package com.nikvay.business_application.activity;

import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity implements VolleyCompleteListener, SuccessDialogClosed, LocationListener {


    private static final String TAG = AttendanceActivity.class.getSimpleName();


    SharedUtil sharedUtil;
    private RadioButton radioButton_attendance;
    private RadioGroup radioGroup_attendance;
    private ImageView iv_back_image_activity;
    private Button btn_submit_attendance;
    private SuccessDialog successDialog;
    TextView text_name, text_mobile_number, text_email_id;
    String attendance, attendanceType;
    String user_id, name, mobile_number, email_id;
    String address;
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        initialize();

        googleApiClient = getAPIClientInstance();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }

        getLocation();
        requestGPSSettings();

    }

    private void initialize() {
        sharedUtil = new SharedUtil(this);
        successDialog = new SuccessDialog(this, true);
        iv_back_image_activity = findViewById(R.id.iv_back_image_activity);
        radioGroup_attendance = findViewById(R.id.radioGroup_attendance);
        btn_submit_attendance = findViewById(R.id.btn_submit_attendance);
        text_name = findViewById(R.id.text_name);
        text_mobile_number = findViewById(R.id.text_mobile_number);
        text_email_id = findViewById(R.id.text_emailId);

        user_id = sharedUtil.getUserDetails().getUser_id();
        name = sharedUtil.getUserDetails().getFirst_name() + " " + sharedUtil.getUserDetails().getLast_name();
        mobile_number = sharedUtil.getUserDetails().getMobile_no();
        email_id = sharedUtil.getUserDetails().getEmail();


        text_name.setText(name);
        text_mobile_number.setText(mobile_number);
        text_email_id.setText(email_id);

        events();
    }

    private void events() {

        iv_back_image_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectedId = radioGroup_attendance.getCheckedRadioButtonId();
                radioButton_attendance = findViewById(selectedId);

                attendance = radioButton_attendance.getText().toString();

                if (attendance.equalsIgnoreCase("PreLunch (9:15 AM)")) {
                    attendanceType = "1";
                } else {
                    attendanceType = "2";
                }
                callAttendanceWS(user_id, attendanceType);


            }
        });
    }

    private void callAttendanceWS(String user_id, String attendanceType) {

//        Log.d(TAG, address);

        if (address != null && !address.equals("")) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ServerConstants.URL, ServerConstants.serverUrl.MARK_ATTENDANCE);
            map.put("login_location", address);
            Log.d(TAG, address);
            map.put("user_id", user_id);
            map.put("attendence_type", attendanceType);
            new MyVolleyPostMethod(this, map, ServerConstants.ServiceCode.MARK_ATTENDANCE, true);
        } else {
            getLocation();
            Toast.makeText(getApplicationContext(), "Please wait for 10 sec...", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case ServerConstants.ServiceCode.MARK_ATTENDANCE: {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error_code = jsonObject.getString("error_code");
                    String msg = jsonObject.getString("msg");
                    if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                        successDialog.showDialog("Attendance Mark Successfully", true);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Attendance Not Mark Successfully", Toast.LENGTH_SHORT).show();

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


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {


        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d(TAG, String.valueOf(addresses.get(0)));
            address = addresses.get(0).getAddressLine(0);

        } catch (Exception e) {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(AttendanceActivity.this, "Please Internet", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    protected void onResume() {

        super.onResume();
        getLocation();
        requestGPSSettings();
    }

    private GoogleApiClient getAPIClientInstance() {
        return new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
    }

    private void requestGPSSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(300);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                       // Toast.makeText(getApplication(), "GPS is already enable", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            status.startResolutionForResult(AttendanceActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                        //Toast.makeText(getApplication(), "Location settings are inadequate, and cannot be fixed here", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


}
