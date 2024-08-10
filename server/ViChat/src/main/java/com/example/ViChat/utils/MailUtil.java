package com.example.ViChat.utils;

public class MailUtil {
    public static final class SEND_EMAIL_SUBJECT {
        public static final String CLIENT_REGISTER = "CONFIRM CREATION OF USER INFORMATION";
        public static final String CLIENT_OTP = "YOUR OTP CODE FOR PASSWORD RESET";
        public static final String CLIENT_NEW_PASSWORD = "NEW PASSWORD FOR YOUR ACCOUNT";

    }

    public static final class TEMPLATE_EMAIL_CRETE_USER {
        public static final String TEMPLATE_GMAIL_REGISTER = "template-gmail";
        public static final String TEMPALTE_SEND_VERIFY_CODE = "send_verify_code";
        public static final String TEMPLATE_SEND_NEW_PASSWORD = "template_send_new_password";
    }
}
