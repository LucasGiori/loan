package application.ports.outbound

import driven.database.OutboxDto
import io.vertx.sqlclient.SqlConnection

interface OutboxEventDAOPort {
    suspend fun push(outboxDto: OutboxDto, connection: SqlConnection)
}