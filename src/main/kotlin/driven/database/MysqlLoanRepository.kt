package driven.database

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId
import application.domain.models.Status
import application.ports.outbound.LoanDAOPort
import application.ports.outbound.LoanRepositoryPort
import application.ports.outbound.LoggerPort
import application.ports.outbound.OutboxEventDAOPort
import io.vertx.mysqlclient.MySQLPool
import jakarta.inject.Inject
import jakarta.inject.Singleton
import driven.database.extension.withTransactionCustom
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class MysqlLoanRepository @Inject constructor(
    private val pool: MySQLPool,
    private val loanDAO: LoanDAOPort,
    private val outboxEventDAO: OutboxEventDAOPort,
    private val logger: LoggerPort,
): LoanRepositoryPort {

    override suspend fun pull(loanId: LoanId): LoanEvent? {
        TODO("Not yet implemented")
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

    override suspend fun push(event: LoanEvent) {
        TODO("Not yet implemented")
    }
}