package query.loan.available

interface GetLoanAvailableDAO {
    suspend fun getLoanAvailable(loanId: String): GetLoanAvailableResult?
}