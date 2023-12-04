package me.shukawam;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/cowsay")
@RegisterRestClient(baseUri = "http://localhost:8080/remote")
public interface CowsayRestClient {

    @GET
    @Path("/say")
    public String say();

    @GET
    @Path("/delay")
    public String delay();
}
