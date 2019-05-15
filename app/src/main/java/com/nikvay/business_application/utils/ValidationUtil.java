package com.nikvay.business_application.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by subham on 20/1/17.
 */

public class ValidationUtil {

    public static String vaildEmailPassword(String email, String password){
        if (email.isEmpty()){
            return "Email ID can't be empty";
        } else if (!emailCheck(email)){
            return "Please enter a valid email address.";
        } else if (password.isEmpty()){
            return "Password can't be empty";
        } else if (password.length() > 15 || password.length() < 5) {
            return "Password length should be min 5 to max 15";
        }
        return "success";
    }

    public static String vaildChangePassword(String old_password, String new_password, String reenter_password){
        if (old_password.isEmpty()){
            return "Old Password can't be empty";
        } else if (old_password.length() > 15 || old_password.length() < 5) {
            return "Old Password length should be min 5 to max 15";
        } else if (new_password.isEmpty()){
            return "New Password can't be empty";
        } else if (new_password.length() > 15 || new_password.length() < 5) {
            return "New Password length should be min 5 to max 15";
        } else if (reenter_password.isEmpty()){
            return "Reenter Password ID can't be empty";
        } else if (reenter_password.length() > 15 || reenter_password.length() < 5) {
            return "Reenter Password length should be min 5 to max 15";
        } else if (!new_password.equals(reenter_password)){
            return "Password does not match";
        }
        return "success";
    }

    public static boolean emailCheck(String email) {
        try {
            Pattern pattern = Pattern.compile("^([a-z0-9\\+_\\-]+)(\\.[a-z0-9\\+_\\-]+)*@([a-z0-9\\-]+\\.)+[a-z]{2,6}$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch(Exception ex) {
            return false;
        }
    }

}
