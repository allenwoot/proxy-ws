package io.heyproxy.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class Hello {

    // This method is called if TEXT_PLAIN is requested
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHelloInPlainText() {
        return "Hello world!";
    }

    // This method is called if HTML is requested
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayHelloInHtml() {
        return "<html> " + "<title>" + "Hello world!" + "</title>"
                + "<body><h1>" + "Hello world!" + "</body></h1>" + "</html> ";
    }
}