package com.jab.proxy.web_service.beans;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Root level object to be used for all server responses, containing
 * two fields, result and error. For 200 responses, the error field
 * should be nonnull.
 */
@JsonPropertyOrder({"result", "error"})
public final class ServerResponse {
    private ServerError error;
    private ServerResult result;

    public ServerResponse(final ServerError error) {
        this.error = error;
    }

    public ServerResponse(final ServerResult result) {
        this.result = result;
    }

    public ServerError getError() {
        return this.error;
    }

    public ServerResult getResult() {
        return this.result;
    }

    public void setError(final ServerError error) {
        this.error = error;
    }

    public void setResult(final ServerResult result) {
        this.result = result;
    }
}
