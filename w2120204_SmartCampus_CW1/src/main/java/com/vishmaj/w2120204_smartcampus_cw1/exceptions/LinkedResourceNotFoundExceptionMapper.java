package com.vishmaj.w2120204_smartcampus_cw1.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        return Response.status(422) // 422
                // Entity
                .entity("{\n  \"error\": \"Unprocessable Entity\",\n  \"message\": \"" + exception.getMessage() + "\"\n}")
                .type("application/json")
                .build();
    }
}