package com.jab.proxy.web_service.beans;

/**
 * Request object to be put inside the result field of responses to
 * translate. Different types of proxy requests, e.g. restaurant
 * reservations, should subclasst this class. This is done for static
 * type checking.
 */
public class ProxyRequest {
    private Intent intent;

    public Intent getIntent() {
        return this.intent;
    }

    public void setIntent(final Intent intent) {
        this.intent = intent;
    }
}
