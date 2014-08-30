package com.jab.proxy.web_service.beans;

/**
 * Client request with a request string to classify
 */
public final class ClientRequest {
    private String request;

    public String getRequest() {
        return this.request;
    }

    public void setRequest(final String request) {
        this.request = request;
    }
}
