package com.jab.proxy.web_service.beans;

public final class ServerError {
    private int code;
    private String message;

    public ServerError() {
    }

    public ServerError(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
