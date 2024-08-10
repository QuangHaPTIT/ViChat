package com.example.ViChat.utils;

import java.security.SecureRandom;
import java.util.Random;

public class VerifyUtil {
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String digits = "0123456789"; // 0-9
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final String generateVerificationCode() {
        int code = RANDOM.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public static final String generatePassword() {
        String password = getRandomString(6) + String.valueOf(RANDOM.nextInt(10) * 10);
        return password;
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHA.length());
            sb.append(ALPHA.charAt(index));
        }
        return sb.toString();
    }
}
