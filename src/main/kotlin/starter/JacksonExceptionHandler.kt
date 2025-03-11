package starter

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import jakarta.ws.rs.core.Response

@Provider
class JacksonExceptionHandler : ExceptionMapper<MismatchedInputException> {
    override fun toResponse(exception: MismatchedInputException?) = Response
        .status(422)
        .entity("422 UNPROCESSABLE_ENTITY")
        .build()
}