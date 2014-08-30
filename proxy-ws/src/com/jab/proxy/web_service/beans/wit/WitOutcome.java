package com.jab.proxy.web_service.beans.wit;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WitOutcome {
    private String _text;
    private Double confidence;
    private WitEntities entities;
    private String intent;

    public String get_text() {
        return this._text;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public WitEntities getEntities() {
        return this.entities;
    }

    public String getIntent() {
        return this.intent;
    }

    public void set_text(final String _text) {
        this._text = _text;
    }

    public void setConfidence(final Double confidence) {
        this.confidence = confidence;
    }

    public void setEntities(final WitEntities entities) {
        this.entities = entities;
    }

    public void setIntent(final String intent) {
        this.intent = intent;
    }
}
