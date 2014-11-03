package com.jab.proxy.web_service.beans;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TranslateResult extends ServerResult {
    private String confirmation;
    private List<TranslationField> missingFields;
    private ProxyRequest request;
    private List<TranslationField> validatedFields;

    public String getConfirmation() {
        return this.confirmation;
    }

    public List<TranslationField> getMissingFields() {
        return this.missingFields;
    }

    public ProxyRequest getRequest() {
        return this.request;
    }

    public List<TranslationField> getValidatedFields() {
        return this.validatedFields;
    }

    public void setConfirmation(final String confirmation) {
        this.confirmation = confirmation;
    }

    public void setMissingFields(final List<TranslationField> missingFields) {
        this.missingFields = missingFields;
    }

    public void setRequest(final ProxyRequest request) {
        this.request = request;
    }

    public void setValidatedFields(final List<TranslationField> validatedFields) {
        this.validatedFields = validatedFields;
    }
}
