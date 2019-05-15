package com.nikvay.business_application.utils;

import com.nikvay.business_application.model.MyCustomerModel;

public interface SelectCustomerInterface {
    public void getCustomerName(String mCustomerName);
    public void getCustomerDetail(MyCustomerModel customerModel);
}
