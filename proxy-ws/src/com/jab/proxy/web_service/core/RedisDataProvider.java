package com.jab.proxy.web_service.core;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.grizzly.http.util.HttpStatus;

import redis.clients.jedis.Jedis;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.RestaurantResRequest;
import com.jab.proxy.web_service.beans.User;
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
    public boolean registerAccount(final User user) throws ProxyException {
        // Check inputs
        if (ProxyUtils.isNullOrWhiteSpace(user.getEmail())) {
            throw new ProxyException(String.format("Missing field {%s}", "email"), HttpStatus.BAD_REQUEST_400);
        } else if (ProxyUtils.isNullOrWhiteSpace(user.getFirstName())) {
            throw new ProxyException(String.format("Missing field {%s}", "firstName"), HttpStatus.BAD_REQUEST_400);
        } else if (ProxyUtils.isNullOrWhiteSpace(user.getLastName())) {
            throw new ProxyException(String.format("Missing field {%s}", "lastName"), HttpStatus.BAD_REQUEST_400);
        } else if (ProxyUtils.isNullOrWhiteSpace(user.getNumber())) {
            throw new ProxyException(String.format("Missing field {%s}", "number"), HttpStatus.BAD_REQUEST_400);
        } else if (ProxyUtils.isNullOrWhiteSpace(user.getPassword())) {
            throw new ProxyException(String.format("Missing field {%s}", "password"), HttpStatus.BAD_REQUEST_400);
        } else if (user.getUserType() == null) {
            throw new ProxyException(String.format("Missing field {%s}", "userType"), HttpStatus.BAD_REQUEST_400);
        }

        if (this.jedis.exists(user.getEmail())) {
            throw new ProxyException(String.format("User %s already registered", user.getEmail()), HttpStatus.FORBIDDEN_403);
        }

        // Create auth for user
        user.setAuthToken(ProxyUtils.generateAuthToken(user));
        this.jedis.set(user.getAuthToken(), user.getEmail());

        // Save user to storage
        final String jsonString = ProxyUtils.toJsonString(user);
        this.jedis.set(user.getEmail(), jsonString);
        return "OK".equals(this.jedis.save());
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

        // Save
        return "OK".equals(this.jedis.save());
    }

    @Override
    public boolean updateAccount(final String authToken, final User user) throws ProxyException {
        if (ProxyUtils.isNullOrWhiteSpace(authToken)) {
            throw new ProxyException("Auth token not provided", HttpStatus.BAD_REQUEST_400);
        } else if (!this.jedis.exists(authToken)) {
            throw new ProxyException("Auth token not recognized", HttpStatus.BAD_REQUEST_400);
        }

        final User storedUser = ProxyUtils.fromJsonString(this.jedis.get(this.jedis.get(authToken)), User.class);
        if (user.getFirstName() != null && user.getFirstName() != storedUser.getFirstName()) {
            storedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null && user.getLastName() != storedUser.getLastName()) {
            storedUser.setLastName(user.getLastName());
        }
        if (user.getNumber() != null && user.getNumber() != storedUser.getNumber()) {
            storedUser.setNumber(user.getNumber());
        }
        if (user.getPassword() != null && user.getPassword() != storedUser.getPassword()) {
            storedUser.setPassword(user.getPassword());
        }
        // If the email is being updated, refresh the auth key reverse map
        if (user.getEmail() != null && user.getEmail() != storedUser.getEmail()) {
            if (this.jedis.exists(user.getEmail())) {
                throw new ProxyException(String.format("Email %s already exists", user.getEmail()), HttpStatus.FORBIDDEN_403);
            }

            this.jedis.del(storedUser.getEmail());
            storedUser.setEmail(user.getEmail());
            this.jedis.set(user.getEmail(), ProxyUtils.toJsonString(storedUser));
            this.jedis.set(authToken, user.getEmail());
        } else {
            this.jedis.set(storedUser.getEmail(), ProxyUtils.toJsonString(storedUser));
        }

        // Save
        return "OK".equals(this.jedis.save());
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

        // Save
        this.jedis.save();
        return restaurantReservationRequest;
    }

    @Override
    public User validateAuthToken(final String authToken) {
        if (!this.jedis.exists(authToken)) {
            return null;
        }

        return ProxyUtils.fromJsonString(this.jedis.get(this.jedis.get(authToken)), User.class);
    }
}
