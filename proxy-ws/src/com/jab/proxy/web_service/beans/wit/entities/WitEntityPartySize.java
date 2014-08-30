package com.jab.proxy.web_service.beans.wit.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WitEntityPartySize {
    private Integer value;

    public Integer getValue() {
        return this.value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }
}
