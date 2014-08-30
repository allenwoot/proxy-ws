package com.jab.proxy.web_service.beans;

/**
 * Client request intent types
 */
public enum Intent {
    RESTAURANT_RESERVATION("make_reservation");

    public static Intent fromSchemaName(final String schemaName) {
        for (final Intent intent : Intent.values()) {
            if (intent.schemaName.equals(schemaName)) {
                return intent;
            }
        }

        return null;
    }

    private String schemaName;

    private Intent(final String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return this.schemaName;
    }
}
