package com.nikvay.business_application.model;

public class CNPProfileSubTypeListModel {
    String id;

    public boolean isSubTypeSelected() {
        return isSubTypeSelected;
    }

    public void setSubTypeSelected(boolean subTypeSelected) {
        isSubTypeSelected = subTypeSelected;
    }

    boolean isSubTypeSelected;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
