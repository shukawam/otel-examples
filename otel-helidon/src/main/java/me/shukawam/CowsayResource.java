package me.shukawam;

import java.util.Optional;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/api/cowsay")
public class CowsayResource {
    
    private static final Logger LOGGER = Logger.getLogger(CowsayResource.class.getName());
    private final CowsayRestClient cowsayRestClient;

    @Inject
    public CowsayResource(CowsayRestClient cowsayRestClient) {
        this.cowsayRestClient = cowsayRestClient;
    }

    @GET
    @Path("/say")
    public String say(@QueryParam("sleep") Optional<Long> ms) {
        LOGGER.info("Inside cowsay:say invoker.");
        return cowsayRestClient.say(ms);
    }
}
