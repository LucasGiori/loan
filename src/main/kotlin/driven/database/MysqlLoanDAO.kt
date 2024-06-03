package driven.database

import application.ports.outbound.LoanDAOPort
import application.ports.outbound.LoggerPort
import driven.database.extension.requireRowCountGreaterThan
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.SqlConnection
import io.vertx.sqlclient.Tuple
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class MysqlLoanDAO @Inject constructor(
    private val logger: LoggerPort,
): LoanDAOPort {
    companion object {
        private const val LOG_KEY = "loan_dao"
        private const val FIND_BY_ID = "SELECT id, customer, status, version, created_at, updated_at FROM loan WHERE id = ?;"
        private const val FIND_BY_VERSION = "SELECT id, customer, status, version, created_at, updated_at FROM loan WHERE id = ? AND version = ?;"
        private const val INSERT = "INSERT INTO loan (id, customer, status, version) VALUES (?, ?, ?, ?);"
        private const val UPDATE = "UPDATE loan SET status = ?, version = ?, proposals = ? WHERE id = ? AND version = ?"
    }

    override suspend fun pull(loanId: String, connection: SqlConnection): LoanDto? {
        try {
            val result = connection.preparedQuery(FIND_BY_ID).execute(Tuple.of(loanId))
            val row = result.await().firstOrNull()

            return row?.let {
                logger.info(key = "${LOG_KEY}_pull_success", data = it.toJson().toString())

                LoanDto(
                    loanId = loanId,
                    customer = row.getJson("customer").toString(),
                    status = row.getString("status"),
                    version = row.getInteger("version"),
                    createdAt = row.getLocalDateTime("created_at"),
                    updatedAt = row.getLocalDateTime("updated_at")
                )
            }
        } catch (throwable: Throwable) {
            logger.error(key = "${LOG_KEY}_pull_error", data = throwable.toString())

            throw throwable
        }
    }

    override suspend fun findByVersion(loanId: String, version: Int, connection: SqlConnection): LoanDto? {
        try {
            val result = connection.preparedQuery(FIND_BY_VERSION).execute(Tuple.of(loanId, version))
            val row = result.await().firstOrNull()

            return row?.let {
                logger.info(key = "${LOG_KEY}_find_by_version_success", data = it.toJson().toString())

                LoanDto(
                    loanId = loanId,
                    customer = row.getJson("customer").toString(),
                    status = row.getString("status"),
                    version = row.getInteger("version"),
                    createdAt = row.getLocalDateTime("created_at"),
                    updatedAt = row.getLocalDateTime("updated_at")
                )
            }
        } catch (throwable: Throwable) {
            logger.error(key = "${LOG_KEY}_find_by_version_error", data = throwable.toString())

            throw throwable
        }
    }

    override suspend fun push(loan: LoanDto, connection: SqlConnection) {
        try {
            val params = Tuple.of(loan.loanId, loan.customer, loan.status, loan.version)

            connection.preparedQuery(INSERT).execute(params).await().requireRowCountGreaterThan(threshold = 0)
        } catch(throwable: Throwable) {
            logger.error(key = "${LOG_KEY}_push_error", data = throwable.toString())

            throw throwable
        }
    }

    override suspend fun update(loan: LoanDto, version: Int, connection: SqlConnection) {
        try {
            logger.info("LÇUCAS", loan.proposals.toString())
            val params = Tuple.of(loan.status, version, loan.proposals.toString(), loan.loanId, version)


            connection.preparedQuery(UPDATE).execute(params).await().requireRowCountGreaterThan(threshold = 0)
        } catch(throwable: Throwable) {
            logger.error(key = "${LOG_KEY}_update_error", data = throwable.toString())

            throw throwable
        }
    }
}