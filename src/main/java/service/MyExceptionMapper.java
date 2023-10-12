package service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyExceptionMapper implements ExceptionMapper<MyException> {
    private static final Logger logger = LoggerFactory.getLogger(MyExceptionMapper.class);

    @Override
    public Response toResponse(MyException exception) {
        // Log the exception (if needed)
        logger.error(exception.getMessage());

        // Customize the response based on your specific requirements
        int status = Response.Status.BAD_REQUEST.getStatusCode();
        String message = "An error occurred: " + exception.getMessage();

        // Build the response
        return Response.status(status).entity(message).build();
    }
}
