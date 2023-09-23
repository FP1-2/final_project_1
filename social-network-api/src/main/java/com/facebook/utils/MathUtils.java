package com.facebook.utils;

import java.security.SecureRandom;

public class MathUtils {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static int random(int min, int max) {
        int range = max - min + 1;
        return secureRandom.nextInt(range) + min;
    }
}
