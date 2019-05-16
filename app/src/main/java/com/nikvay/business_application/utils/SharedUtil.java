package com.nikvay.business_application.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.nikvay.business_application.model.ApplicationData;
import com.nikvay.business_application.model.UserDetails;

import java.util.ArrayList;


public class SharedUtil {
    private Context context;
    private static String DEVICE_TOKEN = "DEVICE_TOKEN";


	public SharedUtil(Context context) {
		super();
		this.context = context;
	}

    public boolean clearShareUtils(){
        SharedPreferences settings = context.getSharedPreferences(StaticContent.UserData.PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        return editor.commit();
    }

    public static void putDeviceToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticContent.UserData.APPLICATION_DATA, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(DEVICE_TOKEN, token)
                .apply();
    }

    public static String getDeviceToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticContent.UserData.APPLICATION_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEVICE_TOKEN, "");
    }


    public boolean addFirebaseId(String text){
        SharedPreferences settings = context.getSharedPreferences(StaticContent.UserData.PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(StaticContent.UserData.REGISTRATION_ID, text);
        return editor.commit();
    }

    public boolean addUserDetails(UserDetails userDetails){
        SharedPreferences settings = context.getSharedPreferences(StaticContent.UserData.PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(StaticContent.UserData.USER_ID, userDetails.getUser_id());
        editor.putString(StaticContent.UserData.FIRST_NAME, userDetails.getFirst_name());
        editor.putString(StaticContent.UserData.LAST_NAME, userDetails.getLast_name());
        editor.putString(StaticContent.UserData.MOBILE_NO, userDetails.getMobile_no());
        editor.putString(StaticContent.UserData.EMAIL, userDetails.getEmail());
        editor.putString(StaticContent.UserData.DOB, userDetails.getDob());
        editor.putString(StaticContent.UserData.GENDER, userDetails.getGender());
        editor.putString(StaticContent.UserData.PROFILE_IMAGE, userDetails.getProfile_image());
        editor.putString(StaticContent.UserData.JOINING_DATE, userDetails.getJoining_date());
        editor.putString(StaticContent.UserData.DATE_TIME, userDetails.getDate_time());
        editor.putString(StaticContent.UserData.STATUS, userDetails.getStatus());
        editor.putString(StaticContent.UserData.BRANCH_ID, userDetails.getBranch_id());

        return editor.commit();
    }

    public UserDetails getUserDetails(){
        SharedPreferences pref = context.getSharedPreferences(StaticContent.UserData.PREFS_NAME, Activity.MODE_PRIVATE);
        String user_id = pref.getString(StaticContent.UserData.USER_ID, null);
        String first_name = pref.getString(StaticContent.UserData.FIRST_NAME, null);
        String last_name = pref.getString(StaticContent.UserData.LAST_NAME, null);
        String mobile_no = pref.getString(StaticContent.UserData.MOBILE_NO, null);
        String email = pref.getString(StaticContent.UserData.EMAIL, null);
        String dob = pref.getString(StaticContent.UserData.DOB, null);
        String gender = pref.getString(StaticContent.UserData.GENDER, null);
        String profile_image = pref.getString(StaticContent.UserData.PROFILE_IMAGE, null);
        String joining_date = pref.getString(StaticContent.UserData.JOINING_DATE, null);
        String date_time = pref.getString(StaticContent.UserData.DATE_TIME, null);
        String status = pref.getString(StaticContent.UserData.STATUS, null);
        String branch = pref.getString(StaticContent.UserData.BRANCH_ID, null);


        UserDetails userDetails = new UserDetails();
        userDetails.setUser_id(user_id);
        userDetails.setFirst_name(first_name);
        userDetails.setLast_name(last_name);
        userDetails.setMobile_no(mobile_no);
        userDetails.setEmail(email);
        userDetails.setDob(dob);
        userDetails.setGender(gender);
        userDetails.setProfile_image(profile_image);
        userDetails.setJoining_date(joining_date);
        userDetails.setDate_time(date_time);
        userDetails.setStatus(status);
        userDetails.setBranch_id(branch);
        return userDetails;
    }

    public void  addapplicationNameImageMessage(String splashImage,String screenImage,String applicationName,String message)
    {
        SharedPreferences settings = context.getSharedPreferences(StaticContent.UserData.APPLICATION_DATA, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("SPLASHIMAGE", splashImage);
        editor.putString("SCREENIMAGE", screenImage);
        editor.putString("APPLICATIONNAME", applicationName);
        editor.putString("MESSAGE", message);
        editor.commit();
    }

    public ArrayList<ApplicationData> getapplicationData()
    {
        ArrayList<ApplicationData> applicationDataArrayList=new ArrayList<>();
        ApplicationData applicationData=new ApplicationData();
        SharedPreferences pref = context.getSharedPreferences(StaticContent.UserData.APPLICATION_DATA, Activity.MODE_PRIVATE);
        String  splash_image = pref.getString("SPLASHIMAGE", null);
        String screen_image = pref.getString("SCREENIMAGE", null);
        String application_name = pref.getString("APPLICATIONNAME", null);
        String message = pref.getString("MESSAGE", null);

        applicationData.setSplash_image(splash_image);
        applicationData.setScreen_image(screen_image);
        applicationData.setApplication_name(application_name);
        applicationData.setMessage(message);

       applicationDataArrayList.add(applicationData);
       return applicationDataArrayList;

    }


}
