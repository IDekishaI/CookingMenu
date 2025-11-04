package com.CM.CookingMenu.common.utils;

public class JsonUtils {
    private JsonUtils(){}

    public static String extractJsonFromText(String text) {
        int jsonStart = text.indexOf('{');
        int jsonEnd = text.lastIndexOf('}');

        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart)
            return text.substring(jsonStart, jsonEnd + 1);

        return text;
    }
}
