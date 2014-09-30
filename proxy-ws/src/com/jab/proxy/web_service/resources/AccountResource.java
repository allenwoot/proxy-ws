package com.jab.proxy.web_service.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import com.jab.proxy.web_service.beans.ServerResponse;
import com.jab.proxy.web_service.beans.User;
import com.jab.proxy.web_service.core.AccountService;
import com.jab.proxy.web_service.exceptions.ProxyException;

/**
 * Defines the account endpoint
 */
@Path("/account")
public class AccountResource {

    @Context
    private Request requestContext;

    @Context
    private HttpServletRequest servletRequest;

    /**
     * Registers a user
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse registerAccount(final User user) throws ProxyException {
        final AccountService accountService = new AccountService();
        return new ServerResponse(accountService.registerAccount(user));
    }

    /**
     * Updates a user
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse updateAccount(final User user) throws ProxyException {
        final AccountService accountService = new AccountService();
        return new ServerResponse(accountService.updateAccount(this.servletRequest.getHeader("Auth-Token"), user));
    }

}
