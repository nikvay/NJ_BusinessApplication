package com.nikvay.business_application.utils;

public class MathCalculation {
    public String getAddedPercentage(String mNetPrice, String mValue) {
        float tempNetPrice=0f,tempValue = 0f,mul,ansHold=0f,ans=0f;
        try {
            tempNetPrice = Float.parseFloat(mNetPrice);
            tempValue = Float.parseFloat(mValue);
            mul = tempValue / 100;
            ansHold = tempNetPrice - (tempNetPrice * mul);
            ans = tempNetPrice - ansHold;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return String.valueOf(ans);
    }

    public float calculatePer(String value, String price) {
        float mValue=0f,mPrice=0f,ans=0f;
        try {
            if (value.isEmpty()) {
                value = "0";
            }
            mValue = Float.parseFloat(value) / 100;
            mPrice = Float.parseFloat(price);
            ans = mPrice - (mPrice * mValue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ans;
    }

    public String add(String first, String second) {
        long a = 0,b=0;
        try {
            a = Long.parseLong(first);
            b = Long.parseLong(second);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return String.valueOf(a + b);
    }

    public String sub(String first, String second) {
        long a = 0,b=0;
        try {
            a = Integer.parseInt(first);
            b = Integer.parseInt(second);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return String.valueOf(a - b);
    }
}
