package com.nikvay.business_application.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.nikvay.business_application.R;
import com.nikvay.business_application.common.CommonVars;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.utils.Logs;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.ShowToast;
import com.nikvay.business_application.utils.ValidationUtil;
import com.nikvay.business_application.volley_support.MyVolleyPostFragmentMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by cts on 20/4/17.
 */

@SuppressLint("ValidFragment")
public class ChangePasswordFragment extends Fragment {

    private static final String TAG = ChangePasswordFragment.class.getSimpleName();
    EditText edt_old_password, edt_new_password, edt_reenter_password;
    TextView txt_error_message;
    Button btn_change_password;
    SharedUtil sharedUtil;
    private Context mContext;

    public ChangePasswordFragment(Context mContext) {
        this.mContext = mContext;
    }

    public interface iChangePasswordFragmentItemClick {
        public void loadHomeFragment();
    }

    public ChangePasswordFragment.iChangePasswordFragmentItemClick iChangePasswordFragmentItemClick;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iChangePasswordFragmentItemClick = (ChangePasswordFragment.iChangePasswordFragmentItemClick) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        sharedUtil = new SharedUtil(getActivity());
        edt_old_password = (EditText) view.findViewById(R.id.edt_old_password);
        edt_new_password = (EditText) view.findViewById(R.id.edt_new_password);
        edt_reenter_password = (EditText) view.findViewById(R.id.edt_reenter_password);
        txt_error_message = (TextView) view.findViewById(R.id.txt_error_message);
        btn_change_password = (Button) view.findViewById(R.id.btn_change_password);

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = edt_old_password.getText().toString();
                String new_password = edt_new_password.getText().toString();
                String reenter_password = edt_reenter_password.getText().toString();

                String status = ValidationUtil.vaildChangePassword(old_password, new_password, reenter_password);

                VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
                    @Override
                    public void onTaskCompleted(String response, int serviceCode) {
                        switch (serviceCode) {
                            case ServerConstants.ServiceCode.CHANGE_PASSWORD:
                                Logs.showLogE(TAG, response);
                                try {
                                    JSONObject object = new JSONObject(response.toString());
                                    int error_code = object.isNull("error_code") ? null : object.getInt("error_code");
                                    String msg = object.isNull("msg") ? null : object.getString("msg");

                                    if (error_code == 1) {
                                        ShowToast.showToast("Change Password Successfully", getActivity());
                                        iChangePasswordFragmentItemClick.loadHomeFragment();
                                    } else {
                                        txt_error_message.setText(msg);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onTaskFailed(String response, int serviceCode) {
                        switch (serviceCode) {
                            case ServerConstants.ServiceCode.CHANGE_PASSWORD:
                                Logs.showLogE(TAG, response);
                                break;
                        }
                    }
                };

                if (status.equals("success")) {
                    txt_error_message.setText("");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(ServerConstants.URL, ServerConstants.serverUrl.CHANGE_PASSWORD);
                    map.put("user_id", sharedUtil.getUserDetails().getUser_id());
                    map.put("old_password", old_password);
                    map.put("new_password", new_password);
                    new MyVolleyPostFragmentMethod(getActivity(), volleyCompleteListener, map, ServerConstants.ServiceCode.CHANGE_PASSWORD, true);
                } else {
                    txt_error_message.setText(status);
                }
            }
        });

    }

    // load HomeScreenFragment
    public void loadHomeFragment() {
        CommonVars.status = "home";
        HomeScreenFragment obj = new HomeScreenFragment(mContext);
        obj.setHasOptionsMenu(true);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, obj).commit();
    }

}