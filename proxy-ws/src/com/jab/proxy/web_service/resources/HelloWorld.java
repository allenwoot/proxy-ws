package com.jab.proxy.web_service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Hello world endpoint for testing
 */
@Path("/hello")
public class HelloWorld {

    /**
     * For testing
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String saySomething() {
        return "wat";
    }
}