package com.nikvay.business_application.utils;

import com.nikvay.business_application.model.ProductModel;

public interface SelectedProductInterface {
    public void addSelectedProduct(ProductModel productModel);
    public void subSelectedProduct(ProductModel productModel);
    public void removeSelectedProduct();
    public void quantityNotify();
}
