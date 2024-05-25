package query.loan.dao

import application.ports.outbound.LoggerPort
import io.vertx.kotlin.coroutines.await
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Tuple
import jakarta.inject.Singleton
import query.loan.available.GetLoanAvailableDAO
import query.loan.available.GetLoanAvailableResult

@Singleton
class GetLoanAvailableMysqlDAO (
    private val pool: MySQLPool,
    private val logger: LoggerPort
): GetLoanAvailableDAO {
    companion object {
        private const val LOG_KEY = "get_loan_available_dao"
        private const val SQL = "SELECT id, customer, status, version, created_at, updated_at FROM loan WHERE id = ?;"
    }

    override suspend fun getLoanAvailable(loanId: String): GetLoanAvailableResult? {
        try {
            val result = pool.preparedQuery(SQL).execute(Tuple.of(loanId)).await()
            val row = result.firstOrNull()

            logger.info(key = "${LOG_KEY}_success", data = row?.toJson().toString())

            return row?.let {
                GetLoanAvailableResult(
                    loanId = loanId,
                    status = row.getString("status"),
                    version = row.getInteger("version")
                )
            }
        } catch (throwable: Throwable) {
            logger.error(key="${LOG_KEY}_error", data=throwable.toString())
            throw Exception(throwable.toString())
        }
    }
}

