package application.ports.outbound

import org.jboss.logging.Logger

interface LoggerPort {
    fun info(key: String, data: Any?, method: String? = null, path: String? = null, statusCode: Int? = null)
    fun warn(key: String, data: Any?, method: String? = null, path: String? = null, statusCode: Int? = null)
    fun error(key: String, data: Any?, method: String? = null, path: String? = null, statusCode: Int? = null)
    suspend fun logAsync(
        key: String,
        level: Logger.Level,
        data: Any?,
        method: String?,
        path: String?,
        statusCode: Int?
    )
}