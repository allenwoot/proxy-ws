package com.jab.proxy.web_service.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;

public enum SingletonDataProvider implements DataProvider {
    INSTANCE();

    private LinkedHashSet<ProxyRequest> allRequests;

    private SingletonDataProvider() {
        this.allRequests = new LinkedHashSet<ProxyRequest>();
    }

    @Override
    public List<ProxyRequest> getRequestsByStatus(final RequestStatus status) {
        final List<ProxyRequest> requestedRequests = new ArrayList<ProxyRequest>();
        for (final ProxyRequest proxyRequest : this.allRequests) {
            if (status.equals(proxyRequest.getStatus())) {
                requestedRequests.add(proxyRequest);
            }
        }

        return requestedRequests;
    }

    @Override
    public boolean submitToQueue(final ProxyRequest proxyRequest) {
        return this.allRequests.add(proxyRequest);
    }
}
