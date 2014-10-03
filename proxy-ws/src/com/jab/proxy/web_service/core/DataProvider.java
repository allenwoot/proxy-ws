package com.jab.proxy.web_service.core;

import java.util.List;

import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RequestStatus;
import com.jab.proxy.web_service.beans.User;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Interface to perform storage related operations
 */
public interface DataProvider {

    /**
     * Authenticates a user given the email and password fields
     */
    public User authenticateUser(User user) throws ProxyException;

    /**
     * Gets all requests with a given status
     */
    public List<ProxyRequest> getRequestsByStatus(RequestStatus status);

    /**
     * Registers a user
     */
    public boolean registerAccount(User user) throws ProxyException;

    /**
     * Submits the specified request to the queue
     */
    public boolean submitToQueue(User user, ProxyRequest proxyRequest) throws ProxyException;

    /**
     * Updates the request with the specified ID with the specified status
     */
    public ProxyRequest updateRequest(String id, RequestStatus status) throws ProxyException;

    /**
     * Returns a user object given that user's ID
     */
    User getUserById(String userId);

    /**
     * Updates a user
     */
    boolean updateAccount(User storedUser, User updatedUser) throws ProxyException;
}