package com.jab.proxy.web_service.core;

import java.util.List;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;

public interface DataProvider {

    public List<ProxyRequest> getRequestsByStatus(RequestStatus status);

    public boolean submitToQueue(ProxyRequest proxyRequest);
}
