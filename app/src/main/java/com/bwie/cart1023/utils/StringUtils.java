package com.bwie.cart1023.utils;

/**
 * Created by eric on 2018/10/22.
 */

public class StringUtils {
    public static String https2Http(String url) {
        return url.replace("https", "http");
    }
}
