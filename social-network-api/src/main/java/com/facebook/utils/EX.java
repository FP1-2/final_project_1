package com.facebook.utils;

import java.util.Map;
import lombok.extern.log4j.Log4j2;

/**
 * Використовується для нереалізованих методів.
 */
@Log4j2
public class EX {
    public static final RuntimeException NI = new RuntimeException("not implemented yet");

    public static void logMapKeysAndValues(Map<String, Object> resultMap) {
        for (String key : resultMap.keySet()) {
            log.info("Key: " + key + ", Value: " + resultMap.get(key));
        }
    }
}
