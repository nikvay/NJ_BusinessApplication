package com.nikvay.business_application.model;

public class ViewQuotationModel {

    String product_discounted_price;
    String discount_value;
    String product_id;
    String name;
    String price;
    String gross_wt;
    String net_wt;
    String dimension;
    String product_qty;
    String mQbasedPrice;
    String mGrandPrice;
    String packing_charges;
    String insurance_charges;
    String freight_terms;
    String SGST;
    String CGST;
    String IGST;
    String mNetPrice;

    public String getGst_type() {
        return gst_type;
    }

    public void setGst_type(String gst_type) {
        this.gst_type = gst_type;
    }

    String gst_type;

    public String getNew_billing_address() {
        return new_billing_address;
    }

    public void setNew_billing_address(String new_billing_address) {
        this.new_billing_address = new_billing_address;
    }

    String new_billing_address;

    public String getPacking_charges() {
        return packing_charges;
    }

    public void setPacking_charges(String packing_charges) {
        this.packing_charges = packing_charges;
    }

    public String getInsurance_charges() {
        return insurance_charges;
    }

    public void setInsurance_charges(String insurance_charges) {
        this.insurance_charges = insurance_charges;
    }

    public String getFreight_terms() {
        return freight_terms;
    }

    public void setFreight_terms(String freight_terms) {
        this.freight_terms = freight_terms;
    }

    public String getSGST() {
        return SGST;
    }

    public void setSGST(String SGST) {
        this.SGST = SGST;
    }

    public String getCGST() {
        return CGST;
    }

    public void setCGST(String CGST) {
        this.CGST = CGST;
    }

    public String getIGST() {
        return IGST;
    }

    public void setIGST(String IGST) {
        this.IGST = IGST;
    }


    public String getmGrandPrice() {
        return mGrandPrice;
    }

    public void setmGrandPrice(String mGrandPrice) {
        this.mGrandPrice = mGrandPrice;
    }

    public String getmQbasedPrice() {
        return mQbasedPrice;
    }

    public void setmQbasedPrice(String mQbasedPrice) {
        this.mQbasedPrice = mQbasedPrice;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_discounted_price() {
        return product_discounted_price;
    }

    public void setProduct_discounted_price(String product_discounted_price) {
        this.product_discounted_price = product_discounted_price;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
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
}
