package com.jab.proxy.web_service.resources;

import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import com.jab.proxy.web_service.beans.ClientRequest;
import com.jab.proxy.web_service.beans.RestaurantReservationRequest;
import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.beans.TranslateResult;

/**
 * Defines the translate endpoint
 */
@Path("/translate")
public class TranslateResource {
    @Context
    private Request request;

    /**
     * Classify a client request into strictly defined fields
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse translateClientRequest(final ClientRequest clientRequest) {
        final TranslateResult translateResult = new TranslateResult();
        final RestaurantReservationRequest restaurantReservationRequest = new RestaurantReservationRequest();
        restaurantReservationRequest.setPartySize(4);
        restaurantReservationRequest.setRestaurant("House of Prime Rib");
        translateResult.setRequest(restaurantReservationRequest);
        translateResult.setMissingFields(Arrays.asList("dateTime"));
        final ServerResponse serverResponse = new ServerResponse(translateResult);
        return serverResponse;
    }
}
