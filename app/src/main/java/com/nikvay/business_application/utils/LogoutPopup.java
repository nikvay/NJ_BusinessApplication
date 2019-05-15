package com.nikvay.business_application.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.nikvay.business_application.activity.LoginActivity;


/**
 * Created by Tamboli on 02-Mar-17.
 */

public class LogoutPopup {

    public static void Logout(final Context context){

        final SharedUtil sharedUtil = new SharedUtil(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        boolean isromved = sharedUtil.clearShareUtils();
                        if (isromved) {
                            Toast.makeText(context, "Logout successfull", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, LoginActivity.class);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        }
                    }

                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
