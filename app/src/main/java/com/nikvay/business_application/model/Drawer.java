package com.nikvay.business_application.model;

import java.io.Serializable;

/**
 * Created by cts on 3/6/17.
 */

public class Drawer implements Serializable {

    int logo;
    String category_name;

    public Drawer() {
    }

    public Drawer(int logo, String category_name) {
        this.logo = logo;
        this.category_name = category_name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
