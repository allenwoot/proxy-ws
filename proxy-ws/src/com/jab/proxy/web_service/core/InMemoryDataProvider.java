package com.jab.proxy.web_service.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.exceptions.ProxyException;

public enum InMemoryDataProvider implements DataProvider {
    INSTANCE();

    private LinkedHashSet<ProxyRequest> allRequests;

    private InMemoryDataProvider() {
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

    @Override
    public ProxyRequest updateRequest(final String id, final RequestStatus status) throws ProxyException {
        if (id == null || status == null) {
            throw new ProxyException("Request must contain an ID and an update status", HttpStatus.BAD_REQUEST_400);
        }

        for (final ProxyRequest proxyRequest : this.allRequests) {
            if (proxyRequest.getId().equals(id)) {
                proxyRequest.setStatus(status);
                return proxyRequest;
            }
        }

        return null;
    }
}
