package query.loan.available

data class GetLoanAvailableResult(
    val id: String,
    val status: String,
    val proposals: Any,
    val version: Int
)