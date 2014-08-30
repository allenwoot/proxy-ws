package com.jab.proxy.web_service.exceptions.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.ServerError;

/**
 * Maps thrown exceptions to an error response. Jersey exception mappers
 * map an exception to the mapper that is closest in the inheritance tree
 * to the exception being thrown.
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception e) {
        final ServerError serverError = new ServerError(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode(), e.getMessage());
        return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode()).entity(serverError).type(MediaType.APPLICATION_JSON).build();
    }
}
