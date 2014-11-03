package com.jab.proxy.web_service.beans;

public enum AllTranslationFields {
    DATE_TIME("dateTime", String.class.getSimpleName(), "When?"),
    PARTY_SIZE("partySize", Integer.class.getSimpleName(), "For how many people?"),
    RESTAURANT("restaurant", String.class.getSimpleName(), "Where?");

    private String friendlyString;

    private String name;
    private String type;

    private AllTranslationFields(final String name, final String type, final String friendlyString) {
        this.name = name;
        this.type = type;
        this.friendlyString = friendlyString;
    }

    public String getName() {
        return this.name;
    }

    public TranslationField getTranslationField() {
        final TranslationField translationField = new TranslationField();
        translationField.setName(this.name);
        translationField.setType(this.type);
        translationField.setFriendlyString(this.friendlyString);

        return translationField;
    }
}
