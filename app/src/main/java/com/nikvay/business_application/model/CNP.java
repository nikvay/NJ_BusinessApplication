package com.nikvay.business_application.model;

import java.util.ArrayList;

/**
 * Created by Tamboli on 28-May-18.
 */

public class CNP {

    int error_code;
    String msg;
    String[] suggestions;
    UserDetails userDetails;
    ArrayList<Product> products;
    ArrayList<ProductStock> productStocks;

    public CNP() {
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String[] getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String[] suggestions) {
        this.suggestions = suggestions;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<ProductStock> getProductStocks() {
        return productStocks;
    }

    public void setProductStocks(ArrayList<ProductStock> productStocks) {
        this.productStocks = productStocks;
    }
}
