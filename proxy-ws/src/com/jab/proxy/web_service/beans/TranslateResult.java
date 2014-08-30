package com.jab.proxy.web_service.beans;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TranslateResult extends ServerResult {
    private List<String> missingFields;
    private ProxyRequest request;

    public List<String> getMissingFields() {
        return this.missingFields;
    }

    public ProxyRequest getRequest() {
        return this.request;
    }

    public void setMissingFields(final List<String> missingFields) {
        this.missingFields = missingFields;
    }

    public void setRequest(final ProxyRequest request) {
        this.request = request;
    }
}
