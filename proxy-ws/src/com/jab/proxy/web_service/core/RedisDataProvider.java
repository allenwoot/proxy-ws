package com.jab.proxy.web_service.core;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.grizzly.http.util.HttpStatus;

import redis.clients.jedis.Jedis;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.RestaurantResRequest;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 *  Uses Redis for storage
 */
public enum RedisDataProvider implements DataProvider {
    INSTANCE;

    private Jedis jedis;

    private RedisDataProvider() {
        this.jedis = new Jedis("localhost");
    }

    @Override
    public List<ProxyRequest> getRequestsByStatus(final RequestStatus status) {
        // Get the ID list from the status
        final List<String> ids = this.jedis.lrange(status.name(), 0, 100);
        final List<ProxyRequest> requests = new ArrayList<ProxyRequest>();

        // For each ID, get the json string value and deserialize it into the object
        for (final String id : ids) {
            final String jsonString = this.jedis.get(id);
            final ProxyRequest proxyRequest = ProxyUtils.fromJsonString(jsonString, RestaurantResRequest.class);
            requests.add(proxyRequest);
        }

        return requests;
    }

    @Override
    public boolean submitToQueue(final ProxyRequest proxyRequest) throws ProxyException {
        if (!(proxyRequest instanceof RestaurantResRequest)) {
            throw new ProxyException("Request is not restaurant request", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        // Set the json string in redis
        final String jsonString = ProxyUtils.toJsonString(proxyRequest);
        this.jedis.set(proxyRequest.getId(), jsonString);

        // Add request to the list of queued IDs
        this.jedis.rpush(RequestStatus.QUEUED.name(), proxyRequest.getId());

        // Background save
        return "OK".equals(this.jedis.bgsave());
    }

    @Override
    public ProxyRequest updateRequest(final String id, final RequestStatus status) throws ProxyException {
        // Get a request object from the redis json string
        final RestaurantResRequest restaurantReservationRequest = ProxyUtils.fromJsonString(this.jedis.get(id), RestaurantResRequest.class);

        // If the update status is the same as the existing status, return
        final RequestStatus oldStatus = restaurantReservationRequest.getStatus();
        if (restaurantReservationRequest.getStatus() == status) {
            return restaurantReservationRequest;
        }

        // Set the status, get the json representation, and set it in redis
        restaurantReservationRequest.setStatus(status);
        final String newJsonString = ProxyUtils.toJsonString(restaurantReservationRequest);
        this.jedis.set(id, newJsonString);

        // Remove from old status list and add to new status list
        this.jedis.lrem(oldStatus.name(), 1, id);
        this.jedis.rpush(status.name(), id);

        // Background save
        this.jedis.bgsave();
        return restaurantReservationRequest;
    }
}
