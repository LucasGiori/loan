package query.loan.available

data class GetLoanAvailableResult(
    val loanId: String,
    val status: String,
    val version: Int
)