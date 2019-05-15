package com.nikvay.business_application.utils;


import com.nikvay.business_application.model.CNP;
import com.nikvay.business_application.model.Product;
import com.nikvay.business_application.model.ProductStock;
import com.nikvay.business_application.model.UserDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tamboli on 28-Feb-17.
 */

public class ResponseUtil {

    public static CNP getUserDetails(String response) {

        CNP cnp = new CNP();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            int error_code = jsonObject.isNull("error_code") ? null : jsonObject.getInt("error_code");
            String msg = jsonObject.isNull("msg") ? null : jsonObject.getString("msg");

            cnp.setError_code(error_code);
            cnp.setMsg(msg);

            JSONArray arrayList = jsonObject.getJSONArray("user_details");
            for (int i = 0; i < arrayList.length(); i++) {
                JSONObject jUserDetails = arrayList.getJSONObject(i);
                String user_id = jUserDetails.isNull("user_id") ? null : jUserDetails.getString("user_id");
                String first_name = jUserDetails.isNull("first_name") ? null : jUserDetails.getString("first_name");
                String last_name = jUserDetails.isNull("last_name") ? null : jUserDetails.getString("last_name");
                String mobile_no = jUserDetails.isNull("mobile_no") ? null : jUserDetails.getString("mobile_no");
                String email_id = jUserDetails.isNull("email_id") ? null : jUserDetails.getString("email_id");
                String password = jUserDetails.isNull("password") ? null : jUserDetails.getString("password");
                String dob = jUserDetails.isNull("dob") ? null : jUserDetails.getString("dob");
                String gender = jUserDetails.isNull("gender") ? null : jUserDetails.getString("gender");
                String profile_image = jUserDetails.isNull("profile_image") ? null : jUserDetails.getString("profile_image");
                String joining_date = jUserDetails.isNull("joining_date") ? null : jUserDetails.getString("joining_date");
                String date_time = jUserDetails.isNull("date_time") ? null : jUserDetails.getString("date_time");
                String status = jUserDetails.isNull("status") ? null : jUserDetails.getString("status");
                String branch = jUserDetails.isNull("branch_id") ? null : jUserDetails.getString("branch_id");

                UserDetails userDetails = new UserDetails();
                userDetails.setUser_id(user_id);
                userDetails.setFirst_name(first_name);
                userDetails.setLast_name(last_name);
                userDetails.setMobile_no(mobile_no);
                userDetails.setEmail(email_id);
                userDetails.setPassword(password);
                userDetails.setDob(dob);
                userDetails.setGender(gender);
                userDetails.setProfile_image(profile_image);
                userDetails.setJoining_date(joining_date);
                userDetails.setDate_time(date_time);
                userDetails.setStatus(status);
                userDetails.setBranch_id(branch);
                cnp.setUserDetails(userDetails);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnp;
    }

    public static CNP getPriceList(String response) {

        CNP cnp = new CNP();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            int error_code = jsonObject.isNull("error_code") ? null : jsonObject.getInt("error_code");
            String msg = jsonObject.isNull("msg") ? null : jsonObject.getString("msg");
            cnp.setError_code(error_code);
            cnp.setMsg(msg);


            ArrayList<Product> products = new ArrayList<Product>();
            JSONArray arrayList = jsonObject.getJSONArray("product_list");
            for (int i = 0; i < arrayList.length(); i++) {

                JSONObject jUserDetails = arrayList.getJSONObject(i);
                String product_id = jUserDetails.isNull("product_id") ? null : jUserDetails.getString("product_id");
                String name = jUserDetails.isNull("name") ? null : jUserDetails.getString("name");
                String price = jUserDetails.isNull("price") ? null : jUserDetails.getString("price");


                Product product = new Product();
                product.setProduct_id(product_id);
                product.setName(name);
                product.setPrice(price);
                products.add(product);
            }

            cnp.setProducts(products);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnp;
    }

    public static CNP getStockList(String response) {

        CNP cnp = new CNP();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            int error_code = jsonObject.isNull("error_code") ? null : jsonObject.getInt("error_code");
            String msg = jsonObject.isNull("msg") ? null : jsonObject.getString("msg");

            cnp.setError_code(error_code);
            cnp.setMsg(msg);

            ArrayList<Product> products = new ArrayList<Product>();
            JSONArray arrayList = jsonObject.getJSONArray("stock_list");
            for (int i = 0; i < arrayList.length(); i++) {
                JSONObject jUserDetails = arrayList.getJSONObject(i);
                String id = jUserDetails.isNull("id") ? null : jUserDetails.getString("id");
                String name = jUserDetails.isNull("name") ? null : jUserDetails.getString("name");
                String stock = jUserDetails.isNull("stock") ? null : jUserDetails.getString("stock");

                Product product = new Product();
                product.setProduct_id(id);
                product.setName(name);
                product.setStock(stock);
                products.add(product);
            }

            cnp.setProducts(products);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnp;
    }

    public static CNP getProductStock(String response) {

        CNP cnp = new CNP();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            int error_code = jsonObject.isNull("error_code") ? null : jsonObject.getInt("error_code");
            String msg = jsonObject.isNull("msg") ? null : jsonObject.getString("msg");

            cnp.setError_code(error_code);
            cnp.setMsg(msg);

            ArrayList<ProductStock> productStocks = new ArrayList<ProductStock>();
            JSONArray arrayList = jsonObject.getJSONArray("product_stock");
            for (int i = 0; i < arrayList.length(); i++) {
                JSONObject jUserDetails = arrayList.getJSONObject(i);
                String stock = jUserDetails.isNull("stock") ? null : jUserDetails.getString("stock");
                String date = jUserDetails.isNull("date") ? null : jUserDetails.getString("date");

                ProductStock productStock = new ProductStock();
                productStock.setQuantity(stock);
                productStock.setDate(date);
                productStocks.add(productStock);
            }

            cnp.setProductStocks(productStocks);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnp;
    }

}
