package com.vishmaj.w2120204_smartcampus_cw1.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/") // this listens to the root of the projects application path (api/v1/)
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiMetaData() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("api_version","v1");
        metadata.put("admin_contact","dimithra.20231454@iit.ac.lk");
        metadata.put("description","Smart Campus Sensor & Room Management API");

        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        metadata.put("_links", links);

        return Response.ok(metadata).build();
    }
}
