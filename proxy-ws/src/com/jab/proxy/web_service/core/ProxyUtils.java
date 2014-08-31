package com.jab.proxy.web_service.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.google.gson.Gson;
import com.jab.proxy.web_service.exceptions.ProxyException;

public class ProxyUtils {

    public static <T> T fromJsonString(final String jsonString, final Class<T> clazz) {
        final Gson gson = new Gson();
        final T t = gson.fromJson(jsonString, clazz);

        return t;
    }

    public static boolean isNullOrWhiteSpace(final String s) {
        return s == null || s.trim().equals("");
    }

    public static String toIso8601Time(final long time) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date(time));
    }

    public static String toJsonString(final Object o) throws ProxyException {
        final ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(Inclusion.NON_NULL);
        try {
            return om.writeValueAsString(o);
        } catch (final IOException e) {
            throw new ProxyException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }
}
