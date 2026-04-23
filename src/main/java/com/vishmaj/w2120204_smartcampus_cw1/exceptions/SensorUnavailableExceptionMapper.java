package com.vishmaj.w2120204_smartcampus_cw1.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        return Response.status(Response.Status.FORBIDDEN) // 403 Forbidden
                .entity("{\n  \"error\": \"Forbidden\",\n  \"message\": \"" + exception.getMessage() + "\"\n}")
                .type("application/json")
                .build();
    }
}