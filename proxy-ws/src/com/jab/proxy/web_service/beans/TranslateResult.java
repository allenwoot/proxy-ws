package com.jab.proxy.web_service.beans;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TranslateResult extends ServerResult {
    private String confirmation;
    private Intent intent;
    private List<TranslationField> missingFields;
    private List<TranslationField> validatedFields;

    public String getConfirmation() {
        return this.confirmation;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public List<TranslationField> getMissingFields() {
        return this.missingFields;
    }

    public List<TranslationField> getValidatedFields() {
        return this.validatedFields;
    }

    public void setConfirmation(final String confirmation) {
        this.confirmation = confirmation;
    }

    public void setIntent(final Intent intent) {
        this.intent = intent;
    }

    public void setMissingFields(final List<TranslationField> missingFields) {
        this.missingFields = missingFields;
    }

    public void setValidatedFields(final List<TranslationField> validatedFields) {
        this.validatedFields = validatedFields;
    }
}
