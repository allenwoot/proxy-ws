package com.jab.proxy.web_service.beans;

public enum RequestStatus {
    DONE,
    PROCESSING,
    QUEUED;

    public static RequestStatus fromString(final String statusString) {
        for (final RequestStatus requestStatus : RequestStatus.values()) {
            if (requestStatus.toString().equals(statusString)) {
                return requestStatus;
            }
        }

        return null;
    }
}
