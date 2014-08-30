package com.jab.proxy.web_service.beans.wit;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.jab.proxy.web_service.beans.wit.entities.WitEntityPartySize;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WitEntities {
    private List<WitEntityPartySize> party;

    public List<WitEntityPartySize> getParty() {
        return this.party;
    }

    public void setParty(final List<WitEntityPartySize> party) {
        this.party = party;
    }
}
