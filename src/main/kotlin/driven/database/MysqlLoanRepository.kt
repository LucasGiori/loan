package driven.database

import application.domain.events.LoanInitializedEvent
import application.domain.events.LoanProposalsIssuedEvent
import application.domain.events.LoanRequestedEvent
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Status
import application.domain.models.Version
import application.domain.models.aggregate.Loan
import application.ports.outbound.LoanRepositoryPort
import application.ports.outbound.LoggerPort
import application.ports.outbound.OutboxEventDAOPort
import driven.database.dml.Loan.Companion.FIND_BY_ID
import driven.database.dml.Loan.Companion.FIND_BY_VERSION
import driven.database.dml.Loan.Companion.INSERT_INITIAL_AGGREGATE
import driven.database.dml.Loan.Companion.UPDATE_AGGREGATE
import driven.database.dml.Loan.Companion.UPDATE_STATUS_ONLY
import driven.database.extension.events.status
import driven.database.extension.requireRowCountGreaterThan
import io.vertx.mysqlclient.MySQLPool
import jakarta.inject.Inject
import jakarta.inject.Singleton
import driven.database.extension.withTransactionCustom
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.SqlConnection
import io.vertx.sqlclient.Tuple
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class MysqlLoanRepository @Inject constructor(
    private val pool: MySQLPool,
    private val outboxEventDAO: OutboxEventDAOPort,
    private val logger: LoggerPort
): LoanRepositoryPort {
    companion object {
        private const val AGGREGATE_TYPE = "Loan"
        private const val LOG_KEY = "loan_repository"
    }

    override suspend fun pull(loanId: LoanId): Loan? {
        return pull(loanId, pool.connection.await())
    }

    private suspend fun pull(loanId: LoanId, connection: SqlConnection): Loan? {
        try {
            val result = connection.preparedQuery(FIND_BY_ID).execute(Tuple.of(loanId.value.toString()))
            val row = result.await().firstOrNull()

            return row?.let {
                logger.info(key = "${LOG_KEY}_pull_success", data = it.toJson().toString())

                AggregateRecoveryFactory.from(it.toJson())
            }
        } catch (throwable: Throwable) {
            logger.error(key = "${LOG_KEY}_pull_error", data = throwable.toString())

            throw throwable
        }
    }

    override suspend fun push(event: LoanInitializedEvent) {
        pool.withTransactionCustom { connection ->
            connection
                .preparedQuery(INSERT_INITIAL_AGGREGATE)
                .execute(
                    Tuple.of(
                        event.loanId.value.toString(),
                        Json.encodeToString(event.customer),
                        Status.INITIALIZED.toString(),
                        event.version.value
                    )
                )
                .await()
                .requireRowCountGreaterThan(threshold = 0)

            val loan = pull(loanId = event.loanId, connection = connection)

            loan?.let {
                outboxEventDAO.push(
                    outboxDto = OutboxDto(
                        type = event.javaClass.simpleName.removeSuffix("Event"),
                        payload = Json.encodeToString(event),
                        identity = event.loanId.value.toString(),
                        desAggregateType = AGGREGATE_TYPE,
                        snapshot = Json.encodeToString(Loan.serializer(), loan)
                    ),
                    connection = connection
                )
            }
        }
    }

    override suspend fun push(event: LoanProposalsIssuedEvent) {
        val oldLoan = findByVersion(event.loanId, event.version.previous())

        oldLoan?.let {
            pool.withTransactionCustom { connection ->
                val params = Tuple.of(
                    event.status.toString(),
                    event.version.value,
                    Json.encodeToString<Proposals>(event.proposals),
                    event.loanId.value.toString(),
                    event.version.previous().value
                )

                connection.preparedQuery(UPDATE_AGGREGATE)
                    .execute(params)
                    .await()
                    .requireRowCountGreaterThan(threshold = 0)

                val updatedLoan = pull(loanId = event.loanId, connection = connection)

                val outboxEventDto = OutboxDto(
                    type = event.javaClass.simpleName.removeSuffix("Event"),
                    payload = Json.encodeToString(event),
                    identity = event.loanId.value.toString(),
                    desAggregateType = AGGREGATE_TYPE,
                    snapshot = Json.encodeToString(updatedLoan)
                )

                outboxEventDAO.push(outboxDto = outboxEventDto, connection = connection)
            }
        } ?: throw RuntimeException()
    }

    override suspend fun push(event: LoanRequestedEvent) {
        val oldLoan = findByVersion(event.loanId, event.version.previous())

        oldLoan?.let {
            pool.withTransactionCustom { connection ->
                val params = Tuple.of(
                    event.status.toString(),
                    event.version.value,
                    event.loanId.value.toString(),
                    event.version.previous().value
                )

                connection.preparedQuery(UPDATE_STATUS_ONLY)
                    .execute(params)
                    .await()
                    .requireRowCountGreaterThan(threshold = 0)

                val updatedLoan = pull(loanId = event.loanId, connection = connection)

                val outboxEventDto = OutboxDto(
                    type = event.javaClass.simpleName.removeSuffix("Event"),
                    payload = Json.encodeToString(event),
                    identity = event.loanId.value.toString(),
                    desAggregateType = AGGREGATE_TYPE,
                    snapshot = Json.encodeToString(updatedLoan)
                )

                outboxEventDAO.push(outboxDto = outboxEventDto, connection = connection)
            }
        } ?: throw RuntimeException()
    }

    private suspend fun findByVersion(loanId: LoanId, version: Version): Loan? {
        val connection = pool.connection.await()
        val result = connection.preparedQuery(FIND_BY_VERSION)
            .execute(Tuple.of(loanId.value.toString(), version.value))

        val row = result.await().firstOrNull()

        return row?.let { AggregateRecoveryFactory.from(it.toJson()) }
    }
}