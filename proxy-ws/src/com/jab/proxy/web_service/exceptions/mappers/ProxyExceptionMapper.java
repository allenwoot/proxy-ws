package com.jab.proxy.web_service.exceptions.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.jab.proxy.web_service.beans.ServerError;
import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.exceptions.ProxyException;

@Provider
public class ProxyExceptionMapper implements ExceptionMapper<ProxyException> {

    @Override
    public Response toResponse(final ProxyException e) {
        final ServerError serverError = new ServerError(e.getHttpStatus().getStatusCode(), e.getMessage());
        final ServerResponse serverResponse = new ServerResponse(serverError);
        return Response.status(e.getHttpStatus().getStatusCode()).entity(serverResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
