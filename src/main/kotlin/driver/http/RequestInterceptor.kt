package driver.http

import application.ports.outbound.LoggerPort
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.ext.Provider
import java.io.ByteArrayOutputStream
import jakarta.inject.Inject
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

@Provider
class RequestInterceptor @Inject constructor(
    private val mapper: ObjectMapper,
    private val log: LoggerPort,
): ContainerRequestFilter {
    override fun filter(request: ContainerRequestContext?) {
        val buffer = ByteArrayOutputStream()
        val requestStream = request?.entityStream
        requestStream?.use { it.copyTo(buffer) }
        val requestData = buffer.toByteArray()

        if (request?.method == "POST") {
            val data = mapper.readValue(requestData, ObjectNode::class.java)

            log.info(
                key = "api::request",
                data = data,
                method = request.method,
                path = request.uriInfo.path,
                statusCode = null,
            )
        }

        request?.entityStream = requestData.inputStream()
    }
}