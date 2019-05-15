package com.nikvay.business_application.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.MainActivity;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.CNP;
import com.nikvay.business_application.model.UserDetails;
import com.nikvay.business_application.utils.CalenderUtil;
import com.nikvay.business_application.utils.CameraGalleryUtil;
import com.nikvay.business_application.utils.Logs;
import com.nikvay.business_application.utils.ResponseUtil;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.utils.ShowToast;
import com.nikvay.business_application.volley_support.AppController;
import com.nikvay.business_application.volley_support.MyVolleyPostFragmentMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by cts on 20/4/17.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static RelativeLayout layout1, layout2;
    public static ImageView img_profile1;
    public static ImageView img_profile2;
    ImageView img_select_image1, img_select_image2;
    EditText edt_first_name, edt_last_name, edt_mobile_number, edt_email_id, edt_date_of_birth, edt_gender;
    TextView txt_error_message;
    Button btn_update;
    SharedUtil sharedUtil;
    String gender = null;
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public interface iProfileFragmentItemClick {
        public void loadHomeFragment();
    }

    public ProfileFragment.iProfileFragmentItemClick iProfileFragmentItemClick;
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iProfileFragmentItemClick = (ProfileFragment.iProfileFragmentItemClick) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        events();
        callWebServices();
        return view;
    }

    private void initView(View view){
        sharedUtil = new SharedUtil(getActivity());
        layout1 = (RelativeLayout) view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
        img_profile1 = (ImageView) view.findViewById(R.id.img_profile1);
        img_profile2 = (ImageView) view.findViewById(R.id.img_profile2);
        img_select_image1 = (ImageView) view.findViewById(R.id.img_select_image1);
        img_select_image2 = (ImageView) view.findViewById(R.id.img_select_image2);
        edt_first_name = (EditText) view.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) view.findViewById(R.id.edt_last_name);
        edt_mobile_number = (EditText) view.findViewById(R.id.edt_mobile_number);
        edt_email_id = (EditText) view.findViewById(R.id.edt_email_id);
        edt_date_of_birth = (EditText) view.findViewById(R.id.edt_date_of_birth);
        edt_gender = (EditText) view.findViewById(R.id.edt_gender);
        txt_error_message = (TextView) view.findViewById(R.id.txt_error_message);
        btn_update = (Button) view.findViewById(R.id.btn_update);
    }

    private void events(){
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = edt_first_name.getText().toString();
                String last_name = edt_last_name.getText().toString();
                String mobile_no = edt_mobile_number.getText().toString();
                String email_id = edt_email_id.getText().toString();
                String date_of_birth = edt_date_of_birth.getText().toString();

                if (sharedUtil.getUserDetails().getUser_id() != null) {

                    UserDetails userDetails = new UserDetails();
                    userDetails.setUser_id(sharedUtil.getUserDetails().getUser_id());
                    if (first_name != null && !first_name.isEmpty()) {
                        userDetails.setFirst_name(first_name);
                    } else {
                        ShowToast.showToast("Please enter first name", getActivity());
                        userDetails.setFirst_name("");
                        return;
                    }

                    if (last_name != null && !last_name.isEmpty()) {
                        userDetails.setLast_name(last_name);
                    } else {
                        ShowToast.showToast("Please enter last name", getActivity());
                        userDetails.setLast_name("");
                        return;
                    }

                    if (mobile_no != null && !mobile_no.isEmpty()) {
                        //ShowToast.showToast("Please enter mobile no", getActivity());
                        userDetails.setMobile_no(mobile_no);
                    } else {
                        userDetails.setMobile_no("");
                    }

                    if (email_id != null && !email_id.isEmpty()) {
                        userDetails.setEmail(email_id);
                    } else {
                        userDetails.setEmail("");
                    }

                    if (date_of_birth != null && !date_of_birth.isEmpty()) {
                        userDetails.setDob(date_of_birth);
                    } else {
                        userDetails.setDob("");
                    }


                    if (gender != null && !gender.isEmpty()) {
                        userDetails.setGender(gender);
                    } else {
                        userDetails.setGender("");
                    }

                    callWebServices(userDetails);

                }

            }
        });

        img_select_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraGalleryUtil.galleryImage(getActivity());
            }
        });

        img_select_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraGalleryUtil.galleryImage(getActivity());
            }
        });

        edt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogGender(getActivity());
            }
        });

        edt_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getActivity(), new CalenderSelectDateListener(),
                        year,
                        month,
                        date);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    //DatePicker class
    class CalenderSelectDateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            try {
                edt_date_of_birth.setText(CalenderUtil.convertDate11(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialogGender(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_gender);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final RadioButton rbMale = (RadioButton) dialog.findViewById(R.id.rbMale);
        final RadioButton rbFemale = (RadioButton) dialog.findViewById(R.id.rbFemale);
        Button btn_done = (Button) dialog.findViewById(R.id.btn_done);

        if (gender != null) {
            if (gender.equals("1")) {
                rbMale.setChecked(true);
            }

            if (gender.equals("2")) {
                rbFemale.setChecked(true);
            }
        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rbMale.isChecked()) {
                    gender = "1";
                    edt_gender.setText("Male");
                }

                if (rbFemale.isChecked()) {
                    gender = "2";
                    edt_gender.setText("Female");
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callWebServices(final UserDetails userDetails) {
        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.UPDATE_USER_INFO:
                        Logs.showLogE(TAG, response);
                        CNP cnp = ResponseUtil.getUserDetails(response);
                        if (cnp.getError_code() == 1) {
                            sharedUtil.addUserDetails(userDetails);
                            iProfileFragmentItemClick.loadHomeFragment();
                            ShowToast.showToast("Profile update successfully", getActivity());
                        }
                        break;
                }
            }

            @Override
            public void onTaskFailed(String response, int serviceCode) {
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.UPDATE_USER_INFO:
                        Logs.showLogE(TAG, response);
                        break;
                }
            }
        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.UPDATE_USER_INFO);
        map.put("user_id", userDetails.getUser_id());
        map.put("first_name", userDetails.getFirst_name());
        map.put("last_name", userDetails.getLast_name());
        map.put("mobile_no", userDetails.getMobile_no());
        map.put("dob", userDetails.getDob());
        map.put("gender", userDetails.getGender());
        if (MainActivity.image_base != null){
            map.put("profile_image", MainActivity.image_base);
        }
        new MyVolleyPostFragmentMethod(getActivity(), volleyCompleteListener, map, ServerConstants.ServiceCode.UPDATE_USER_INFO, true);
    }

    private void callWebServices() {

        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                switch (serviceCode){
                    case ServerConstants.ServiceCode.USER_INFO:
                        Logs.showLogE(TAG + " User Info",  response);
                        CNP cnp = ResponseUtil.getUserDetails(response);
                        if (cnp.getError_code() == 1){
                            sharedUtil.addUserDetails(cnp.getUserDetails());
                            edt_first_name.setText(cnp.getUserDetails().getFirst_name());
                            edt_last_name.setText(cnp.getUserDetails().getLast_name());
                            edt_mobile_number.setText(cnp.getUserDetails().getMobile_no());
                            edt_email_id.setText(cnp.getUserDetails().getEmail());
                            edt_date_of_birth.setText(cnp.getUserDetails().getDob());

                            if (cnp.getUserDetails().getGender() != null) {
                                if (cnp.getUserDetails().getGender().equals("1")) {
                                    gender = "1";
                                    edt_gender.setText("Male");
                                }
                                if (cnp.getUserDetails().getGender().equals("2")) {
                                    gender = "2";
                                    edt_gender.setText("Female");
                                }
                            }

                        }
                        break;
                }
            }

            @Override
            public void onTaskFailed(String response, int serviceCode) {
                switch (serviceCode){
                    case ServerConstants.ServiceCode.USER_INFO:
                        Logs.showLogE(TAG,  response);
                        break;
                }
            }
        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.USER_INFO);
        map.put("user_id", sharedUtil.getUserDetails().getUser_id());
        new MyVolleyPostFragmentMethod(getActivity(), volleyCompleteListener, map, ServerConstants.ServiceCode.USER_INFO, true);
    }

}
