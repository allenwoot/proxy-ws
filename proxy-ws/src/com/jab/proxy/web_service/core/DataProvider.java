package com.jab.proxy.web_service.core;

import java.util.List;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Interface to perform storage related operations
 */
public interface DataProvider {

    public List<ProxyRequest> getRequestsByStatus(RequestStatus status);

    public boolean submitToQueue(ProxyRequest proxyRequest);

    ProxyRequest updateRequest(String id, RequestStatus status) throws ProxyException;
}