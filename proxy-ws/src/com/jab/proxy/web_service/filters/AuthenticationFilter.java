package com.jab.proxy.web_service.filters;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.ServerError;
import com.jab.proxy.web_service.core.StorageClient;
import com.jab.proxy.web_service.utilities.ProxyUtils;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(final ContainerRequest request) {
        // Auth token header is not required for the account setup endpoint
        if ((request.getPath().endsWith("account") && request.getMethod().equalsIgnoreCase("POST")) ||
                request.getPath().endsWith("authenticate") && request.getMethod().equalsIgnoreCase("POST") ||
                    request.getPath().endsWith("hello")) {
            return request;
        }

        // Auth token must be present
        final String authToken = request.getHeaderValue("Auth-Token");
        if (ProxyUtils.isNullOrWhiteSpace(authToken)) {
            throw failRequest(HttpStatus.UNAUTHORIZED_401, "Auth-Token header missing");
        }

        // Validate the auth token
        final String userId = ProxyUtils.extractIdFromAuthToken(authToken);
        if (userId == null || StorageClient.INSTANCE.getDataProvider().getUserById(userId) == null) {
            throw failRequest(HttpStatus.UNAUTHORIZED_401, "Invalid Auth-Token header");
        }

        return request;
    }

    private WebApplicationException failRequest(final HttpStatus httpStatus, final String message) {
        final ServerError serverError = new ServerError(httpStatus.getStatusCode(), message);
        final ResponseBuilder responseBuilder = Response.status(httpStatus.getStatusCode()).type(MediaType.APPLICATION_JSON).entity(serverError);
        return new WebApplicationException(responseBuilder.build());
    }
}
