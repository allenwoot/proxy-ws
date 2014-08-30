package com.jab.proxy.web_service.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import com.jab.proxy.web_service.beans.GetQueueResult;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.RestaurantReservationRequest;
import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.core.RequestService;
import com.jab.proxy.web_service.core.SingletonDataProvider;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Defines the request endpoint
 */
@Path("/request")
public class RequestResource {

    @Context
    private Request requestContext;

    /**
     * Gets proxy requests that have the specified status
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse getRequestsByStatus(@QueryParam("status") final String status) {
        return new ServerResponse(new GetQueueResult(SingletonDataProvider.INSTANCE.getRequestsByStatus(RequestStatus.valueOf(status))));
    }

    /**
     * Submit specified proxy request into queue
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse submitRequestToQueue(final RestaurantReservationRequest proxyRequest) throws ProxyException {
        // TODO: One endpoint for all types of requests
        // This can be done with message body readers
        final RequestService requestService = new RequestService(this.requestContext);
        return new ServerResponse(requestService.submitToRequestQueue(proxyRequest));
    }
}
