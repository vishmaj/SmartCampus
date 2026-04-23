package com.vishmaj.w2120204_smartcampus_cw1.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        
        //addes so that it leaves normal not found 404 errors the same
        if (exception instanceof WebApplicationException) {
            WebApplicationException webEx = (WebApplicationException) exception;
            return webEx.getResponse();
        }
        // Logs the actual error to the server console for the developer to see
        exception.printStackTrace();

        // Returns a safe, generic message to the client
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\n  \"error\": \"Internal Server Error\",\n  \"message\": \"An unexpected error occurred. Please contact the administrator.\"\n}")
                .type("application/json")
                .build();
    }
}