package com.jab.proxy.web_service.beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Request object to be put inside the result field of responses to
 * translate. Different types of proxy requests, e.g. restaurant
 * reservations, should subclasst this class. This is done for static
 * type checking.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyRequest {
    private Intent intent;
    private RequestStatus status;

    public Intent getIntent() {
        return this.intent;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public void setIntent(final Intent intent) {
        this.intent = intent;
    }

    public void setStatus(final RequestStatus status) {
        this.status = status;
    }
}
