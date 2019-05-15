package com.nikvay.business_application.utils;

public interface QuotationUpdateNotifier {
    public void updateQuotation(int mPosition,String mQuantity);
    public void deleteQuotation(int mPosition);
}
