package com.nikvay.business_application.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nikvay.business_application.R;

public class LoginAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private TextView textDialogLoginMessage;
    private Button btnDialogOk;
    private UserData userData;

    public LoginAlertDialog(Context mContext) {
        this.mContext = mContext;
        this.userData = new UserData(mContext);
        this.dialog = new Dialog(mContext);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setCancelable(false);
        this.dialog.setContentView(R.layout.dialog_login_alert);
        this.textDialogLoginMessage = (TextView) dialog.findViewById(R.id.textDialogLoginMessage);
        this.btnDialogOk = (Button) dialog.findViewById(R.id.btnDialogOk);
    }

    public void showLoginAlertDialog(String mMessage) {
        textDialogLoginMessage.setText(mMessage);
        dialog.show();
        dialogEvent();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void dialogEvent() {
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            userData.clearLocalUserData();
                dialog.dismiss();
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        Intent intent = new Intent(StaticContent.LocalBrodcastReceiverCode.CLOSE_ACTIVITY);
        // You can also include some extra data.
        intent.putExtra(StaticContent.LocalBrodcastReceiverCode.CLOSE_ACTIVITY, StaticContent.LocalBrodcastReceiverCode.CLOSE_ACTIVITY);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
