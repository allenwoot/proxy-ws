package com.jab.proxy.web_service.core;

import java.util.List;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Interface to perform storage related operations
 */
public interface DataProvider {

    /**
     * Gets all requests with a given status
     */
    public List<ProxyRequest> getRequestsByStatus(RequestStatus status);

    /**
     * Submits the specified request to the queue
     */
    public boolean submitToQueue(ProxyRequest proxyRequest) throws ProxyException;

    /**
     * Updates the request with the specified ID with the specified status
     */
    ProxyRequest updateRequest(String id, RequestStatus status) throws ProxyException;
}