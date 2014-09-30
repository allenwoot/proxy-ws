package com.jab.proxy.web_service.core;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.User;
import com.jab.proxy.web_service.beans.UserAuthResult;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Provides a service to perform account related tasks
 */
public class AccountService {

    public UserAuthResult authenticateUser(final User user) throws ProxyException {
        final User fetchedUser = StorageClient.INSTANCE.getDataProvider().authenticateUser(user);
        final UserAuthResult userAuthResult = new UserAuthResult();
        userAuthResult.setUser(fetchedUser);

        return userAuthResult;
    }

    public UserAuthResult registerAccount(final User user) throws ProxyException {
        if (!StorageClient.INSTANCE.getDataProvider().registerAccount(user)) {
            throw new ProxyException("Failed to register user", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        final UserAuthResult userAuthResult = new UserAuthResult();
        userAuthResult.setUser(user);

        return userAuthResult;
    }

    public UserAuthResult updateAccount(final String authToken, final User user) throws ProxyException {
        if (!StorageClient.INSTANCE.getDataProvider().updateAccount(authToken, user)) {
            throw new ProxyException("Failed to update user", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        final UserAuthResult userAuthResult = new UserAuthResult();
        userAuthResult.setUser(user);

        return userAuthResult;
    }
}
