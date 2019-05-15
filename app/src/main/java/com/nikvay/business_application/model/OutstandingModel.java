package com.nikvay.business_application.model;

public class OutstandingModel {
    String c_id;
    String company_name;
    String email_id;
    String amount;
    String ref_num;
    String date;
    String due_date;
    String overdue_by_days;

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRef_num() {
        return ref_num;
    }

    public void setRef_num(String ref_num) {
        this.ref_num = ref_num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getOverdue_by_days() {
        return overdue_by_days;
    }

    public void setOverdue_by_days(String overdue_by_days) {
        this.overdue_by_days = overdue_by_days;
    }
}
