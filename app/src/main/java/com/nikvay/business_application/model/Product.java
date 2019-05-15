package com.nikvay.business_application.model;

import java.io.Serializable;

/**
 * Created by Tamboli on 28-May-18.
 */

public class Product implements Serializable {

    String product_id, name, price, stock, date;

    public Product() {
    }

    public Product(String product_id, String name, String price) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
    }

    public Product(String product_id, String name, String stock, String date) {
        this.product_id = product_id;
        this.name = name;
        this.stock = stock;
        this.date = date;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
