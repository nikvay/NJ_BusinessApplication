package com.nikvay.business_application.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nikvay.business_application.R;

public class InternetErrorDialog {
    private Context mContext;
    private Dialog dialog;
    private TextView textDisplayMesageDialog;


    public InternetErrorDialog(Context mContext) {
        this.mContext = mContext;
        this.dialog = new Dialog(mContext);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(R.layout.internet_error_dialog);
        this.textDisplayMesageDialog = dialog.findViewById(R.id.textDisplayMesageDialog);
    }

    public void showDialog(String message) {
        dialog.show();

        textDisplayMesageDialog.setText(message);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

            }
        }, 3000);

    }
}
