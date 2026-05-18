package com.pharmaconnect.automation.utils;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private static ThreadLocal<Map<String, String>> context =
            ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, String value) {
        context.get().put(key, value);
    }

    public static String get(String key) {
        return context.get().get(key);
    }

    public static void clear() {
        context.get().clear();
    }
    public static String getOrDefault(String key, String defaultValue) {
        String value = context.get().get(key);
        return value != null ? value : defaultValue;
    }
}