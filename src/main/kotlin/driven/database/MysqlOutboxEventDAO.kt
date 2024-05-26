package driven.database

import application.ports.outbound.LoggerPort
import application.ports.outbound.OutboxEventDAOPort
import driven.database.extension.requireRowCountGreaterThan
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.SqlConnection
import io.vertx.sqlclient.Tuple
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class MysqlOutboxEventDAO @Inject constructor(
    private val logger: LoggerPort,
): OutboxEventDAOPort {
    companion object {
        private const val LOG_KEY = "outbox_event_dao"
        private const val INSERT = "INSERT INTO outbox_event (cod_outbox_event, des_aggregate_type, identity, type, revision, payload, snapshot) VALUES (?, ?, ?, ?, ?, ?, ?);"
    }
    override suspend fun push(outboxDto: OutboxDto, connection: SqlConnection) {
        try {
            val params = Tuple.of(
                outboxDto.codOutboxEvent,
                outboxDto.desAggregateType,
                outboxDto.identity,
                outboxDto.type,
                outboxDto.revision,
                outboxDto.payload,
                outboxDto.snapshot
            )

            connection.preparedQuery(INSERT).execute(params).await().requireRowCountGreaterThan(threshold = 0)

            logger.info(key = "${LOG_KEY}_success", data = Json.encodeToString(outboxDto))
        } catch(throwable: Throwable) {
            logger.error(key = "${LOG_KEY}_error", data = throwable.toString())

            throw throwable
        }
    }
}