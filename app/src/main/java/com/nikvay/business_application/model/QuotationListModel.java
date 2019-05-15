package com.nikvay.business_application.model;

public class QuotationListModel {
    String quote_num;
    String cust_name;
    String tot_amount;
    String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getQuote_num() {
        return quote_num;
    }

    public void setQuote_num(String quote_num) {
        this.quote_num = quote_num;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getTot_amount() {
        return tot_amount;
    }

    public void setTot_amount(String tot_amount) {
        this.tot_amount = tot_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
