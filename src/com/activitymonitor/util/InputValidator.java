package com.activitymonitor.util;

import java.util.regex.Pattern;

/**
 * Validasi input sederhana untuk keperluan UI/controller.
 */
public class InputValidator {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{3,50}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]{2,100}$",
            Pattern.UNICODE_CHARACTER_CLASS);

    public static boolean isValidUsername(String username) {
        if (username == null)
            return false;
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidFullName(String fullName) {
        if (fullName == null)
            return false;
        return NAME_PATTERN.matcher(fullName.trim()).matches();
    }

    /**
     * Password minimal 6 karakter (aturan demo).
     */
    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;
        return password.length() >= 6;
    }

    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static String requireNotBlank(String s, String fieldName) {
        if (!isNotBlank(s)) {
            throw new IllegalArgumentException(fieldName + " tidak boleh kosong");
        }
        return s.trim();
    }
}
