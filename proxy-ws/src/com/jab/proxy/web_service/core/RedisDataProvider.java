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
import com.jab.proxy.web_service.utilities.ProxyUtils;

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
    public User authenticateUser(final User user) throws ProxyException {
        // Verify email and password are non empty
        if (ProxyUtils.isNullOrWhiteSpace(user.getEmail())) {
            throw new ProxyException("Email must be provided", HttpStatus.BAD_REQUEST_400);
        } else if (ProxyUtils.isNullOrWhiteSpace(user.getPassword())) {
            throw new ProxyException("Password must be provied", HttpStatus.BAD_REQUEST_400);
        }

        // Verify user exists
        if (!this.jedis.exists(user.getEmail())) {
            throw new ProxyException(String.format("User with email %s not found", user.getEmail()), HttpStatus.NOT_FOUND_404);
        }

        // Verify password
        final String userId = this.jedis.get(user.getEmail());
        final User fetchedUser = ProxyUtils.fromJsonString(this.jedis.get(userId), User.class);
        if (!fetchedUser.getPassword().equals(user.getPassword())) {
            throw new ProxyException("Authentication failed", HttpStatus.FORBIDDEN_403);
        }

        return fetchedUser;
    }

    @Override
    public List<ProxyRequest> getRequestsByStatus(final User user, final RequestStatus status) {
        // Get the ID list from the status
        List<String> ids = null;
        switch (user.getUserType()) {
        case USER:
            ids = this.jedis.lrange(user.getId() + status.name(), 0, 10);
            break;
        case WORKER:
            ids = this.jedis.lrange(status.name(), 0, 10);
            break;
        }

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
    public User getUserById(final String userId) {
        return ProxyUtils.fromJsonString(this.jedis.get(userId), User.class);
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

        // Save to storage
        final String jsonString = ProxyUtils.toJsonString(user);
        this.jedis.set(user.getId(), jsonString);
        this.jedis.set(user.getEmail(), user.getId());
        return "OK".equals(this.jedis.save());
    }

    @Override
    public boolean submitToQueue(final User user, final ProxyRequest proxyRequest) throws ProxyException {
        if (!(proxyRequest instanceof RestaurantResRequest)) {
            throw new ProxyException("Request is not restaurant request", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        // Set the json string in redis
        final String jsonString = ProxyUtils.toJsonString(proxyRequest);
        this.jedis.set(proxyRequest.getId(), jsonString);

        // Add request to the list of queued IDs
        this.jedis.rpush(RequestStatus.QUEUED.name(), proxyRequest.getId());
        this.jedis.rpush(user.getId() + RequestStatus.QUEUED.name(), proxyRequest.getId());

        // Save
        return "OK".equals(this.jedis.save());
    }

    @Override
    public boolean updateAccount(final User storedUser, final User updatedUser) throws ProxyException {
        if (updatedUser.getFirstName() != null && updatedUser.getFirstName() != storedUser.getFirstName()) {
            storedUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null && updatedUser.getLastName() != storedUser.getLastName()) {
            storedUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getNumber() != null && updatedUser.getNumber() != storedUser.getNumber()) {
            storedUser.setNumber(updatedUser.getNumber());
        }
        if (updatedUser.getPassword() != null && updatedUser.getPassword() != storedUser.getPassword()) {
            storedUser.setPassword(updatedUser.getPassword());
        }
        // If the email is being updated
        if (updatedUser.getEmail() != null && updatedUser.getEmail() != storedUser.getEmail()) {
            if (this.jedis.exists(updatedUser.getEmail())) {
                throw new ProxyException(String.format("Email %s already exists", updatedUser.getEmail()), HttpStatus.FORBIDDEN_403);
            }

            // Delete the reverse key for the old email
            this.jedis.del(storedUser.getEmail());
            storedUser.setEmail(updatedUser.getEmail());
            this.jedis.set(updatedUser.getEmail(), storedUser.getId());
            this.jedis.set(storedUser.getId(), ProxyUtils.toJsonString(storedUser));
        } else {
            this.jedis.set(storedUser.getId(), ProxyUtils.toJsonString(storedUser));
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

        // Remove from old global status list and add to new global status list
        this.jedis.lrem(oldStatus.name(), 1, id);
        this.jedis.rpush(status.name(), id);

        // Remove from old user status list and add to new user status list
        this.jedis.lrem(restaurantReservationRequest.getRequesterId() + oldStatus.name(), 1, id);
        this.jedis.rpush(restaurantReservationRequest.getRequesterId() + status.name(), id);

        // Save
        this.jedis.save();
        return restaurantReservationRequest;
    }
}
