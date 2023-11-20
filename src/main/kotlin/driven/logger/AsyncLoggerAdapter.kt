package driven.logger

import application.ports.outbound.LoggerPort
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.*
import org.jboss.logging.Logger
import org.jboss.logging.Logger.Level
import org.jboss.logging.MDC

@ApplicationScoped
class AsyncLoggerAdapter(val mapper: ObjectMapper): LoggerPort {

    private val log: Logger = Logger.getLogger(this.javaClass)
    private val requestScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun info(key: String, data: Any?, method: String?, path: String?, statusCode: Int?) {
        requestScope.launch {
            logAsync(key = key, level=Level.INFO, data = data, method = method, path = path, statusCode = statusCode)
        }
    }

    override fun warn(key: String, data: Any?, method: String?, path: String?, statusCode: Int?) {
        requestScope.launch {
            logAsync(key = key, level=Level.WARN, data = data, method = method, path = path, statusCode = statusCode)
        }
    }

    override fun error(key: String, data: Any?, method: String?, path: String?, statusCode: Int?) {
        requestScope.launch {
            logAsync(key = key, level=Level.ERROR, data = data, method = method, path = path, statusCode = statusCode)
        }
    }

    override suspend fun logAsync(
        key: String,
        level: Level,
        data: Any?,
        method: String?,
        path: String?,
        statusCode: Int?
    ) {
        log.log(level, msg(key, data, method, path, statusCode))
    }

    private fun msg(key: String, data: Any?, method: String?, path: String?, statusCode: Int?): String {
        val json = mapper.writeValueAsString(data)
        MDC.put("key", key)
        MDC.put("data", json)
        return "method=$method path=$path statusCode=$statusCode"
    }

    fun onDestroy() {
        requestScope.cancel()
    }
}