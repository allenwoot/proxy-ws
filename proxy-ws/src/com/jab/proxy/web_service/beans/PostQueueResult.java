package com.jab.proxy.web_service.beans;

public class PostQueueResult extends ServerResult {
    private ProxyRequest request;

    public PostQueueResult() {
    }

    public PostQueueResult(final ProxyRequest request) {
        this.request = request;
    }

    public ProxyRequest getRequest() {
        return this.request;
    }

    public void setRequest(final ProxyRequest request) {
        this.request = request;
    }
}
