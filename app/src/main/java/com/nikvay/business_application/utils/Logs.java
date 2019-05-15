package com.nikvay.business_application.utils;

import android.util.Log;

/**
 * Created by Tamboli on 23-Feb-17.
 */

public class Logs {

    public static void showLogV(String tag, String message){
        Log.v(tag, message);
    }

    public static void showLogD(String tag, String message){
        Log.d(tag, message);
    }

    public static void showLogI(String tag, String message){
        Log.i(tag, message);
    }

    public static void showLogW(String tag, String message){
        Log.w(tag, message);
    }

    public static void showLogE(String tag, String message){
        Log.e(tag, message);
    }

}
