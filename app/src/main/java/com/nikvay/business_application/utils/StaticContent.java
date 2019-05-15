package com.nikvay.business_application.utils;

public class StaticContent {

    public class UserData {
        public static final String PREFS_NAME = "AppPrefsFile";

        public static final String REGISTRATION_ID = "regId";

        public static final String USER_ID = "user_id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String MOBILE_NO = "mobile_no";
        public static final String EMAIL = "email";
        public static final String DOB = "dob";
        public static final String GENDER = "gender";
        public static final String PROFILE_IMAGE = "profile_image";
        public static final String JOINING_DATE = "joining_date";
        public static final String DATE_TIME = "date_time";
        public static final String STATUS = "status";
        public static final String BRANCH_ID = "branch";
    }

    public class DrawerItems {
        public static final String HOME = "Home";
        public static final String MY_ACCOUNT = "My Account";
        public static final String NOTIFICATION = "Notification";
        public static final String CHECK_PRICE = "Check Price";
        public static final String CHECK_STOCK = "Check Stock";
        public static final String MARK_ATTENDANCE="Mark Attendance";
        public static final String CHANGE_PASSWORD = "Change Password";
        public static final String MY_CUSTOMER = "My Customer";
        public static final String EXPLODED_VIEW="Exploded View";
        public static final String QUOTATION = "Quotation";
        public static final String LEAVE_APPLICATION= "Leave Application";
        public static final String HOLIDAY_LIST= "Holiday List";
        public static final String MY_PERFORMANCE= "My Performance";

        public static final String LOGOUT = "Logout";
    }

    public class FragmentType {
        public static final String CHECK_STOCK = "Check Stock";
        public static final String CHECK_PRICE = "Check Price";
    }

    public class ServerResponseValidator {
        public static final String MSG = "success";
        public static final String ERROR_CODE = "1";
    }

    public class ActivityType {
        public static final String ACTIVITY_TYPE = "activity-type";
        public static final String VIEW_COLLECTION = "view-collection";
        public static final String VIEW_VISITS = "view-visits";
    }

    public class QuotationStatusCode {
        public static final String ONE = "Waiting For Approval";
        public static final String TWO = "Approved by admin";
        public static final String THREE = "Waiting for customer";
        public static final String FOUR = "Order Received";//customer approved
        public static final String ZERO = "Lost Quotation";
        public static final String FIVE = "Ready for Approval";
        public static final String SIX = "PI";
        public static final String SEND_MAIL = "Mail to Customer";
        public static final String SEND_REMINDER = "Reminder to Customer";
        public static final String SEND_FOR_APPROVAL = "Send for Approval";

    }

    public class IntentType {
        public static final String QUOTATION_COUNT = "quotation-count";
        public static final String QUOTATION_NUMBER = "quotation-number";

    }

    public class IntentKey {
        public static final String CUSTOMER_ID = "quotation-count";
        public static final String CUSTOMER_DETAIL = "customer-detail";
        public static final String QUOTATION_NAME = "quotation-name";
        public static final String DISCOUNT_LIMIT = "discount-limit";
        public static final String TOTAL_OUTSTANDING = "total-outstanding";
        public static final String ACTIVITY_TYPE = "activity-type";

    }

    public class IntentValue {
        public static final String ACTIVITY_EDIT_CUSTOMER = "Edit Customer";

    }

    public class ProductType {
        public static final String LOCAL = "local";
        public static final String EXPORT = "export";

    }


    public class LoginAlertCode {
        public static final String RE_LOGIN_CODE = "555";

    }

    public class LocalBrodcastReceiverCode {
        public static final String CLOSE_ACTIVITY = "close-activity";

    }

    public class GstType {
        public static final String WITHIN_STATE = "within_state";
        public static final String OUTSIDE_STATE = "outsite_state ";

    } public class StockStatus {
        public static final String EX_STOCK = "Ex-Stock";
        public static final String DELIVERY = "Delivery in 8-9 week";

    }
}
