package edu.thinktank.togglz.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class Resource {

    @GET
    public String getMessage() {
        String message = (Feature.FEATURE_ONE.isActive() ? "feature one enabled" : "feature one disabled") + ", ";
        message += Feature.FEATURE_TWO.isActive() ? "feature two enabled" : "feature two disabled";
        return message;
    }

}
