package com.jab.proxy.web_service.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ProxyUtils {

    public static boolean isNullOrWhiteSpace(final String s) {
        return s == null || s.trim().equals("");
    }

    public static String toIso8601Time(final long time) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date(time));
    }
}
