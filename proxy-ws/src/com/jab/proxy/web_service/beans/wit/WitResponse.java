package com.jab.proxy.web_service.beans.wit;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import ai.wit.sdk.model.WitOutcome;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WitResponse {
    private String _text;
    private String msg_id;
    private List<WitOutcome> outcomes;

    public String get_text() {
        return this._text;
    }

    public String getMsg_id() {
        return this.msg_id;
    }

    public List<WitOutcome> getOutcomes() {
        return this.outcomes;
    }

    public void set_text(final String _text) {
        this._text = _text;
    }

    public void setMsg_id(final String msg_id) {
        this.msg_id = msg_id;
    }

    public void setOutcomes(final List<WitOutcome> outcomes) {
        this.outcomes = outcomes;
    }
}
