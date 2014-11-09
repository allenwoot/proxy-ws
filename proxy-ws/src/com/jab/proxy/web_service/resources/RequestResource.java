package com.jab.proxy.web_service.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.RestaurantResRequest;
import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.beans.User;
import com.jab.proxy.web_service.core.RequestService;
import com.jab.proxy.web_service.core.StorageClient;
import com.jab.proxy.web_service.exceptions.ProxyException;
import com.jab.proxy.web_service.utilities.ProxyUtils;

/**
 * Defines the request endpoint
 */
@Path("/request")
public class RequestResource {

    @Context
    private Request requestContext;

    @Context
    private HttpServletRequest servletRequest;

    /**
     * Gets proxy requests that have the specified status
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse getRequestsByStatus(@QueryParam("status") final String status) throws ProxyException {
        final RequestService requestService = new RequestService(this.requestContext);
        final String userId = ProxyUtils.extractIdFromAuthToken(this.servletRequest.getHeader("Auth-Token"));
        final User user = StorageClient.INSTANCE.getDataProvider().getUserById(userId);
        return new ServerResponse(requestService.getRequestsByStatus(user, RequestStatus.fromString(status)));
    }

    /**
     * Submit specified proxy request into queue
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse submitRequestToQueue(final RestaurantResRequest proxyRequest) throws ProxyException {
        // TODO: One endpoint for all types of requests
        // This can be done with message body readers
        final RequestService requestService = new RequestService(this.requestContext);
        final String userId = ProxyUtils.extractIdFromAuthToken(this.servletRequest.getHeader("Auth-Token"));
        final User user = StorageClient.INSTANCE.getDataProvider().getUserById(userId);
        return new ServerResponse(requestService.submitToRequestQueue(user, proxyRequest));
    }

    /**
     * Updates the request with the specified ID with the specified status
     */
    @PUT
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse updateRequest(@PathParam("id") final String id, final ProxyRequest proxyRequest) throws ProxyException {
        final RequestService requestService = new RequestService(this.requestContext);
        return new ServerResponse(requestService.updateRequest(id, proxyRequest.getStatus()));
    }
}
