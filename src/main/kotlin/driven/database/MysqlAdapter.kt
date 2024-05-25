package driven.database

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId
import application.domain.models.Status
import application.domain.models.toJson
import application.ports.outbound.LoanRepositoryPort
import application.ports.outbound.LoggerPort
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Tuple
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class MysqlAdapter @Inject constructor(
    private val pool: MySQLPool,
    private val logger: LoggerPort,
): LoanRepositoryPort {
    companion object {
        const val LOG_KEY = "repository"
    }

    override fun pull(loanId: LoanId): LoanEvent? {
        TODO("Not yet implemented")
    }

    override fun push(event: LoanInitializedEvent) {
        val query = "INSERT INTO loan (id, customer, status, version) VALUES (?, ?, ?, ?);"
        val params = Tuple.of(
            event.loanId,
            event.customer.toJson(),
            Status.INITIALIZED.toString(),
            event.version.value
        )

        pool.preparedQuery(query)
            .execute(params)
            .onFailure{ throwable -> logger.error(key="${LOG_KEY}_error", data=throwable.toString()) }
            .onSuccess{ rows -> logger.info(key="${LOG_KEY}_success", data=event.toString()) }
    }

    override fun push(event: LoanEvent) {
        TODO("Not yet implemented")
    }
}