package com.facebook.utils;

import java.security.SecureRandom;

public class MathUtils {

    private MathUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    public static int random(int min, int max) {
        int range = max - min + 1;
        return secureRandom.nextInt(range) + min;
    }
}
