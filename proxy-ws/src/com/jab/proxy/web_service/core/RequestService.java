package com.jab.proxy.web_service.core;

import javax.ws.rs.core.Request;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.PostQueueResult;
import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.RestaurantReservationRequest;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Provides a service to serve requests to the request endpoint
 */
public class RequestService {
    private final Request requestContext;

    public RequestService(final Request requestContext) {
        this.requestContext = requestContext;
    }

    public PostQueueResult submitToRequestQueue(final ProxyRequest proxyRequest) throws ProxyException {
        if (proxyRequest == null) {
            throw new ProxyException("No proxy request specified", HttpStatus.BAD_REQUEST_400.getStatusCode());
        } else if (proxyRequest.getIntent() == null) {
            throw new ProxyException("Intent must be specified", HttpStatus.BAD_REQUEST_400.getStatusCode());
        }

        switch (proxyRequest.getIntent()) {
        case RESTAURANT_RESERVATION:
            final RestaurantReservationRequest restaurantReservationRequest = (RestaurantReservationRequest) proxyRequest;
            if (ProxyUtils.isNullOrWhiteSpace(restaurantReservationRequest.getDateTime())) {
                throw new ProxyException("Date time must be specified", HttpStatus.BAD_REQUEST_400.getStatusCode());
            } else if (restaurantReservationRequest.getPartySize() == null || restaurantReservationRequest.getPartySize() <= 0) {
                throw new ProxyException("Valid party size must be specified", HttpStatus.BAD_REQUEST_400.getStatusCode());
            }

            final String currentTime = ProxyUtils.toIso8601Time(System.currentTimeMillis());
            restaurantReservationRequest.setCreated(currentTime);
            restaurantReservationRequest.setStatus(RequestStatus.QUEUED);
            restaurantReservationRequest.setId(Integer.toString(currentTime.hashCode()));

            // Put in data provider
            SingletonDataProvider.INSTANCE.submitToQueue(proxyRequest);
        }

        return new PostQueueResult(proxyRequest);
    }
}
