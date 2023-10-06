package com.example.javata.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidatorUtil {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
