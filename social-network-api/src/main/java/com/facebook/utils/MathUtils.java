package com.facebook.utils;

public class MathUtils {
    public static int random(int min, int max) {
        int range = max - min + 1; // 0 .... max - min + 1
        double rand = Math.random() * range;
        return (int) (rand + min);
    }
}
