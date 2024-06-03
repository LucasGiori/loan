package driven.database

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId
import application.domain.models.Status
import application.domain.models.Version
import application.domain.models.aggregate.Loan
import application.ports.outbound.LoanDAOPort
import application.ports.outbound.LoanRepositoryPort
import application.ports.outbound.LoggerPort
import application.ports.outbound.OutboxEventDAOPort
import driven.database.extension.events.status
import io.vertx.mysqlclient.MySQLPool
import jakarta.inject.Inject
import jakarta.inject.Singleton
import driven.database.extension.withTransactionCustom
import io.vertx.kotlin.coroutines.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

@Singleton
class MysqlLoanRepository @Inject constructor(
    private val pool: MySQLPool,
    private val loanDAO: LoanDAOPort,
    private val outboxEventDAO: OutboxEventDAOPort,
    private val logger: LoggerPort
): LoanRepositoryPort {

    override suspend fun pull(loanId: LoanId): Loan? {
        val loanDto = loanDAO.pull(loanId.value.toString(), pool.connection.await())

        return loanDto?.let { AggregateRecoveryFactory.from(it) }
    }

    override suspend fun push(event: LoanInitializedEvent) {
        val loanDto = LoanDto(
            loanId = event.loanId.value.toString(),
            customer = Json.encodeToString(event.customer),
            status = Status.INITIALIZED.toString(),
            version = event.version.value
        )

        pool.withTransactionCustom { connection ->
            loanDAO.push(loan = loanDto, connection = connection)

            val loanDtoResult = loanDAO.pull(loanId = event.loanId.value.toString(), connection = connection)

            val outboxEventDto = OutboxDto(
                type = event.javaClass.simpleName.removeSuffix("Event"),
                payload = Json.encodeToString(event),
                identity = event.loanId.value.toString(),
                snapshot = Json.encodeToString(loanDtoResult)
            )

            outboxEventDAO.push(outboxDto = outboxEventDto, connection = connection)
        }
    }

    private suspend fun findByLoanVersion(loanId: LoanId, version: Version): Loan? {
        val loanDto = loanDAO.findByVersion(
            loanId = loanId.value.toString(),
            version = version.value,
            connection = pool.connection.await()
        )

        return loanDto?.let { AggregateRecoveryFactory.from(it) }
    }

    override suspend fun push(event: LoanEvent) {
        val oldLoan = findByLoanVersion(event.loanId, event.version.previous())
        logger.info(key = "push_test_version", data = event.version)
        logger.info(key="event_serializer_LUCASD", data = Json.encodeToString(event))

        oldLoan?.let {
            val loanDto = LoanDto(
                loanId = event.loanId.value.toString(),
                proposals = null,//if (event is LoanProposalsIssuedEvent)  Json.encodeToString<Proposals>(event.proposals) else null, // @TODO Ajustar isso, é só para fazer funcionar...
                status = event.status.name,
                version = event.version.value,
                updatedAt = LocalDateTime.now()
            )

            pool.withTransactionCustom { connection ->
                loanDAO.update(loan = loanDto, version = event.version.previous().value, connection = connection)

                val loanDtoResult = loanDAO.pull(loanId = event.loanId.value.toString(), connection = connection)

                val outboxEventDto = OutboxDto(
                    type = event.javaClass.simpleName.removeSuffix("Event"),
                    payload = Json.encodeToString(event),
                    identity = event.loanId.value.toString(),
                    snapshot = Json.encodeToString(loanDtoResult)
                )

                outboxEventDAO.push(outboxDto = outboxEventDto, connection = connection)
            }
        } ?: throw RuntimeException()

    }
}