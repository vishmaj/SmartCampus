package com.vishmaj.w2120204_smartcampus_cw1.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        // We globally format the error as a 409 Conflict with a clean JSON payload
        return Response.status(Response.Status.CONFLICT)
                .entity("{\n  \"error\": \"Conflict\",\n  \"message\": \"" + exception.getMessage() + "\"\n}")
                .type("application/json")
                .build();
    }
}