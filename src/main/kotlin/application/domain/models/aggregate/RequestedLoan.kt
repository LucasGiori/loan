package application.domain.models.aggregate

import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RequestedLoan")
data class RequestedLoan(
    override val version: Version,
    override val identity: LoanId,
    val proposals: Proposals
) : Loan
