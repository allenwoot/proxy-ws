package com.jab.proxy.web_service.beans;

import java.util.List;

public class GetQueueResult extends ServerResult {
    private List<ProxyRequest> requests;

    public GetQueueResult() {
    }

    public GetQueueResult(final List<ProxyRequest> requests) {
        this.requests = requests;
    }

    public List<ProxyRequest> getRequests() {
        return this.requests;
    }

    public void setRequests(final List<ProxyRequest> requests) {
        this.requests = requests;
    }
}
