package com.nikvay.business_application.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public UserData(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences = mContext.getSharedPreferences(StaticContent.UserData.PREFS_NAME, Activity.MODE_PRIVATE);
    }

    public boolean setUserRequestFilledData(String mKey, String mValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mKey, mValue);
        return editor.commit();

    }

    public String getUserRequestFilledData(String mName) {

        return sharedPreferences.getString(mName, "");
    }
    public String getUserData(String mName) {

        return sharedPreferences.getString(mName, "");
    }

    public boolean clearLocalUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }
}
