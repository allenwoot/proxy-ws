package com.jab.proxy.web_service.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import com.jab.proxy.web_service.beans.ClientRequest;
import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.core.WitService;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Defines the translate endpoint
 */
@Path("/translate")
public class TranslateResource {

    @Context
    private Request requestContext;

    /**
     * Classify a client request into strictly defined fields
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse translateClientRequest(final ClientRequest clientRequest) throws ProxyException {
        final WitService witService = new WitService();
        return new ServerResponse(witService.translate(clientRequest.getRequest()));
    }
}
