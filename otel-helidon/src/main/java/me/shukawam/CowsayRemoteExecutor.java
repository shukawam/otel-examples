package me.shukawam;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ricksbrown.cowsay.Cowsay;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/remote/cowsay")
public class CowsayRemoteExecutor {
    private static final Logger LOGGER = Logger.getLogger(CowsayRemoteExecutor.class.getName());

    @GET
    @Path("/say")
    public String say(@QueryParam("sleep") Optional<Long> ms) {
        try {
            if (ms.isPresent()) {
                Thread.sleep(ms.get());
            }
            return Cowsay.say(new String[] { "-f", "default", "Moo!" });
        } catch (InterruptedException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
