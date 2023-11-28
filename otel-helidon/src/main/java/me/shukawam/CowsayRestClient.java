package me.shukawam;

import java.util.Optional;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/cowsay")
@RegisterRestClient(baseUri = "http://localhost:8080/remote")
public interface CowsayRestClient {

    @GET
    @Path("/say")
    public String say(@QueryParam("sleep") Optional<Long> ms);
}
