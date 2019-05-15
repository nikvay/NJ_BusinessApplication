package com.nikvay.business_application.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


/**
 * Created by cts on 31/5/17.
 */

public class CommonIntent {

    public static void openUrl(Context context, String url){
        //String url = "http://www.example.com";
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
