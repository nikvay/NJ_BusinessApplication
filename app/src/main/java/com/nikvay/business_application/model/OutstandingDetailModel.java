package com.nikvay.business_application.model;

public class OutstandingDetailModel {
    String amount;
    String ref_num;
    String date;
    String due_date;
    String overdue_by_days;

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
