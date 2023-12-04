package me.shukawam;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ricksbrown.cowsay.Cowsay;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/remote/cowsay")
public class CowsayRemoteExecutor {
    private static final Logger LOGGER = Logger.getLogger(CowsayRemoteExecutor.class.getName());
    private static final long DELAY = 2000;
    private final Tracer tracer;

    @Inject
    public CowsayRemoteExecutor(Tracer tracer) {
        this.tracer = tracer;
    }

    @GET
    @Path("/say")
    public String say(@QueryParam("sleep") Optional<Long> ms) {
        return Cowsay.say(new String[] { "-f", "default", "Moo!" });
    }

    @GET
    @Path("/delay")
    public String delaySay() {
        delayWithCDISpan();
        return Cowsay
                .say(new String[] { "-f", "default", String.format("I'm sleeping in %s ms", Long.toString(DELAY)) });
    }

    @WithSpan(value = "cdi.span")
    private void delayWithCDISpan() {
        delayWithSpanBuilder();
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void delayWithSpanBuilder() {
        Span span = tracer.spanBuilder("builder.span")
                .setSpanKind(SpanKind.INTERNAL)
                .setParent(Context.current())
                .setAttribute("my.attribute", "Hello MP Telemetry Tracing world.")
                .startSpan();
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
        span.end();
    }
}
