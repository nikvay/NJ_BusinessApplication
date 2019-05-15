package com.nikvay.business_application.model;

public class ProductModel {
    String product_id;
    String name;
    String price;
    String gross_wt;
    String net_wt;
    String discountNumber;
    String discount_limit;
    String quantity;
    String showPriceAfterDiscount;
    String stock_status;

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    String accessories;

    public String getStock_count() {
        return stock_count;
    }

    public void setStock_count(String stock_count) {
        this.stock_count = stock_count;
    }

    String stock_count;

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    String product_description;

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }

    boolean isApplied;

    public String getShowPriceAfterDiscount() {
        return showPriceAfterDiscount;
    }

    public void setShowPriceAfterDiscount(String showPriceAfterDiscount) {
        this.showPriceAfterDiscount = showPriceAfterDiscount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount_limit() {
        return discount_limit;
    }

    public void setDiscount_limit(String discount_limit) {
        this.discount_limit = discount_limit;
    }

    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(String discountNumber) {
        this.discountNumber = discountNumber;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    String priceAfterDiscount;

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

    public String getGross_wt() {
        return gross_wt;
    }

    public void setGross_wt(String gross_wt) {
        this.gross_wt = gross_wt;
    }

    public String getNet_wt() {
        return net_wt;
    }

    public void setNet_wt(String net_wt) {
        this.net_wt = net_wt;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    String dimension;
}
