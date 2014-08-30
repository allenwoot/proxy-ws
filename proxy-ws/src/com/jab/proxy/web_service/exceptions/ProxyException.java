package com.jab.proxy.web_service.exceptions;

@SuppressWarnings("serial")
public class ProxyException extends Exception {
    private final Integer status;

    public ProxyException(final String message, final Integer status) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
