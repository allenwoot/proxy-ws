package com.jab.proxy.web_service.exceptions;

import org.glassfish.grizzly.http.util.HttpStatus;

@SuppressWarnings("serial")
public class ProxyException extends Exception {
    private final HttpStatus httpStatus;

    public ProxyException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
