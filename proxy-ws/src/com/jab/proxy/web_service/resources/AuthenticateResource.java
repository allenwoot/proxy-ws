package com.jab.proxy.web_service.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.beans.User;
import com.jab.proxy.web_service.core.AccountService;
import com.jab.proxy.web_service.exceptions.ProxyException;

@Path("/authenticate")
public class AuthenticateResource {

    /**
     * Authenticates a user
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse registerAccount(final User user) throws ProxyException {
        final AccountService accountService = new AccountService();
        return new ServerResponse(accountService.authenticateUser(user));
    }
}
