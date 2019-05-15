package com.nikvay.business_application.model;

import java.io.Serializable;

/**
 * Created by Tamboli on 28-May-18.
 */

public class ProductStock implements Serializable {

    String id, quantity, date;

    public ProductStock() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
