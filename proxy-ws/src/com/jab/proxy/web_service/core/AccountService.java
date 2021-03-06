package com.jab.proxy.web_service.core;

import org.glassfish.grizzly.http.util.HttpStatus;

import com.jab.proxy.web_service.beans.User;
import com.jab.proxy.web_service.beans.UserAuthResult;
import com.jab.proxy.web_service.exceptions.ProxyException;
import com.jab.proxy.web_service.utilities.GmailNotifier;
import com.jab.proxy.web_service.utilities.ProxyUtils;

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
        // Create ID and auth token for user
        user.setId(ProxyUtils.generateUserId(user));
        user.setAuthToken(ProxyUtils.generateAuthToken(user));

        if (!StorageClient.INSTANCE.getDataProvider().registerAccount(user)) {
            throw new ProxyException("Failed to register user", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        final UserAuthResult userAuthResult = new UserAuthResult();
        userAuthResult.setUser(user);

        GmailNotifier.notifyRegisteredUser(user);
        return userAuthResult;
    }

    public UserAuthResult updateAccount(final User storedUser, final User updatedUser) throws ProxyException {
        if (!StorageClient.INSTANCE.getDataProvider().updateAccount(storedUser, updatedUser)) {
            throw new ProxyException("Failed to update user", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        final UserAuthResult userAuthResult = new UserAuthResult();
        userAuthResult.setUser(storedUser);

        return userAuthResult;
    }
}
