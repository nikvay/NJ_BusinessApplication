package com.nikvay.business_application.utils;

import android.content.Context;
import android.widget.Toast;

import com.nikvay.business_application.model.MyCustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCustomerResponse {
    private Context mContext;

    public MyCustomerResponse(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<MyCustomerModel> getCustomerResponse(String response) {
        MyCustomerModel modelC;
        ArrayList<MyCustomerModel> arrayListC;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String error_code = jsonObject.getString("error_code");
            String msg = jsonObject.getString("msg");
            if (error_code.equals(StaticContent.ServerResponseValidator.ERROR_CODE) && msg.equals(StaticContent.ServerResponseValidator.MSG)) {

                JSONArray jsonArray = jsonObject.getJSONArray("customer_list");
                if (jsonArray.length() > 0) {
                    arrayListC = new ArrayList<>();
                    arrayListC.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jdata = jsonArray.getJSONObject(i);
                        String c_id = jdata.getString("c_id");
                        String sale_person_id = jdata.getString("sale_person_id");
                        String date_of_registration = jdata.getString("date_of_registration");
                        String company_name = jdata.getString("company_name");
                        String billing_address = jdata.getString("billing_address");
                        String location = jdata.getString("location");
                        String state = jdata.getString("state");
                        String pincode = jdata.getString("pincode");
                        String address = location + " " + state + " " + pincode;
                        String billing_contact_person = jdata.getString("billing_contact_person");
                        String tel_no = jdata.getString("tel_no");
                        String cell_no = jdata.getString("cell_no");
                        String email_id = jdata.getString("email_id");
                        String billing_GST_no = jdata.getString("billing_GST_no");
                        String packing_charges = jdata.getString("packing_charges");
                        String insurance_charges = jdata.getString("insurance_charges");
                        String term_of_payment = jdata.getString("term_of_payment");
                        String freight_terms = jdata.getString("freight_terms");
                        String discount = jdata.getString("discount");
                        String outstanding_amount = jdata.getString("outstanding_amount");
                        String budget = jdata.getString("budget");
                        String sale = jdata.getString("sale");

                        modelC = new MyCustomerModel();
                        modelC.setC_id(c_id);
                        modelC.setDiscount(discount);
                        modelC.setOutstanding_amount(outstanding_amount.equals("null")?"0":outstanding_amount);
                        modelC.setSale_person_id(sale_person_id);
                        modelC.setDate_of_registration(date_of_registration);
                        modelC.setCompany_name(company_name);
                        modelC.setBilling_address(billing_address);
                        modelC.setLocation(location);
                        modelC.setState(state);
                        modelC.setPincode(pincode);
                        modelC.setAddress(address);
                        modelC.setBilling_contact_person(billing_contact_person);
                        modelC.setTel_no(tel_no);
                        modelC.setCell_no(cell_no);
                        modelC.setEmail_id(email_id);
                        modelC.setBilling_GST_no(billing_GST_no);
                        modelC.setPacking_charges(packing_charges);
                        modelC.setInsurance_charges(insurance_charges);
                        modelC.setTerm_of_payment(term_of_payment);
                        modelC.setFreight_terms(freight_terms);
                        modelC.setBudget(budget);
                        modelC.setSale(sale);
                        arrayListC.add(modelC);
                    }
                    return arrayListC;
                } else {
                    Toast.makeText(mContext, "No Customer Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "No Customer Found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "No Customer Found", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}

