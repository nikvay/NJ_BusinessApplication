package com.nikvay.business_application.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Tamboli on 23-Feb-17.
 */

public class ShowToast {

    public static void showToast(String msg, Context context){
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
    }

    public static void testingToast(String msg, Context context){
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
    }

}
