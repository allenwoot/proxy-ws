package com.jab.proxy.web_service.beans;

public class TranslationField {
    private String friendlyString;
    private String name;
    private String type;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFriendlyString() {
        return this.friendlyString;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public void setFriendlyString(final String friendlyString) {
        this.friendlyString = friendlyString;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
