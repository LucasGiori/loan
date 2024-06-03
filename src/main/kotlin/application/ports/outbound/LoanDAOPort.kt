package application.ports.outbound

import driven.database.LoanDto
import io.vertx.sqlclient.SqlConnection

interface LoanDAOPort {
    suspend fun pull(loanId: String, connection: SqlConnection): LoanDto?
    suspend fun findByVersion(loanId: String, version: Int, connection: SqlConnection): LoanDto?
    suspend fun push(loan: LoanDto, connection: SqlConnection)
    suspend fun update(loan: LoanDto, version: Int, connection: SqlConnection)
}