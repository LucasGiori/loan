package driver.http

import application.ports.outbound.LoggerPort
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider

@Provider
class ResponseInterceptor @Inject constructor(
    private val log: LoggerPort,
    private val mapper: ObjectMapper,
) : ContainerResponseFilter {
    companion object {
        const val KEY = "api::response"
        const val HELLO_PATH = "/hello"
    }

    override fun filter(request: ContainerRequestContext?, response: ContainerResponseContext?) {
        val path = request?.uriInfo?.path
        val isHelloPath = path.equals(other = HELLO_PATH)

        val entity = response?.entity
        val isNullEntity = entity == null

        if (isHelloPath || isNullEntity) return

        val statusCode = response?.status
        val method = request?.method
        var body: ObjectNode

        try {
            body = mapper.valueToTree<ObjectNode>(entity)
        } catch (e: Exception) {
            body = mapper.createObjectNode()
            body.put("message", entity.toString())
        }

        when {
            statusCode == null -> return

            statusCode in 200..399 -> log.info(
                key = KEY,
                data = body,
                method = method,
                path = path,
                statusCode = statusCode,
            )

            statusCode in 400..499 -> log.warn(
                key = KEY,
                data = body,
                method = method,
                path = path,
                statusCode = statusCode,
            )

            statusCode >= 500 -> log.error(
                key = KEY,
                data = body,
                method = method,
                path = path,
                statusCode = statusCode,
            )

            else -> return
        }
    }
}