package com.greatit.demo.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.kafka.streams.KafkaStreams;

import com.greatit.demo.service.StreamService;

@Path("happiness")
@Produces(MediaType.APPLICATION_JSON)
public class HappinessResource {
    private final KafkaStreams streams;

    public HappinessResource(final KafkaStreams streams) {
        this.streams = streams;
    }

    // DI via HK2
    @Inject
    private StreamService streamService;


    @Path("/{key}")
    @GET
    public String hello(@PathParam("key") String key) {
        return streamService.getHello(key, streams);
    }
}
