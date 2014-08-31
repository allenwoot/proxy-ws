package com.jab.proxy.web_service.core;

import javax.ws.rs.core.Request;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.GetQueueResult;
import com.jab.proxy.web_service.beans.PostQueueResult;
import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.RestaurantResRequest;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Provides a service to serve requests to the request endpoint
 */
public class RequestService {
    private final Request requestContext;

    public RequestService(final Request requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Gets all results from the queue with the specified status
     */
    public GetQueueResult getRequestsByStatus(final RequestStatus status) throws ProxyException {
        if (status == null) {
            throw new ProxyException("Valid status must be specified", HttpStatus.BAD_REQUEST_400);
        }

        return new GetQueueResult(StorageClient.INSTANCE.getDataProvider().getRequestsByStatus(status));
    }

    /**
     * Queues the specified request
     */
    public PostQueueResult submitToRequestQueue(final ProxyRequest proxyRequest) throws ProxyException {
        if (proxyRequest == null) {
            throw new ProxyException("No proxy request specified", HttpStatus.BAD_REQUEST_400);
        } else if (proxyRequest.getIntent() == null) {
            throw new ProxyException("Intent must be specified", HttpStatus.BAD_REQUEST_400);
        }

        switch (proxyRequest.getIntent()) {
        case RESTAURANT_RESERVATION:
            final RestaurantResRequest restaurantReservationRequest = (RestaurantResRequest) proxyRequest;
            if (ProxyUtils.isNullOrWhiteSpace(restaurantReservationRequest.getDateTime())) {
                throw new ProxyException("Date time must be specified", HttpStatus.BAD_REQUEST_400);
            } else if (restaurantReservationRequest.getPartySize() == null || restaurantReservationRequest.getPartySize() <= 0) {
                throw new ProxyException("Valid party size must be specified", HttpStatus.BAD_REQUEST_400);
            }

            final String currentTime = ProxyUtils.toIso8601Time(System.currentTimeMillis());
            restaurantReservationRequest.setCreated(currentTime);
            restaurantReservationRequest.setStatus(RequestStatus.QUEUED);
            restaurantReservationRequest.setId(Integer.toString(Math.abs(currentTime.hashCode())));

            // Put in data provider
            StorageClient.INSTANCE.getDataProvider().submitToQueue(proxyRequest);
        }

        return new PostQueueResult(proxyRequest);
    }

    /**
     * Updates the request with the specified ID with the specified status
     */
    public PostQueueResult updateRequest(final String id, final RequestStatus status) throws ProxyException {
        final PostQueueResult postQueueResult = new PostQueueResult();
        final ProxyRequest proxyRequest = StorageClient.INSTANCE.getDataProvider().updateRequest(id, status);
        if (proxyRequest == null) {
            throw new ProxyException("Request with the specified ID not found", HttpStatus.NOT_FOUND_404);
        }

        postQueueResult.setRequest(proxyRequest);
        return postQueueResult;
    }
}
