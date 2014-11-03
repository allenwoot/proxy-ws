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
    private String id;
    private Intent intent;
    private String requesterId;
    private RequestStatus status;

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        }

        final String objId = ((ProxyRequest) obj).getId();
        return objId != null && objId.equals(this.id);
    }

    public String getId() {
        return this.id;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public String getRequesterId() {
        return this.requesterId;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setIntent(final Intent intent) {
        this.intent = intent;
    }

    public void setRequesterId(final String requesterId) {
        this.requesterId = requesterId;
    }

    public void setStatus(final RequestStatus status) {
        this.status = status;
    }
}
