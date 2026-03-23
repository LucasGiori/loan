package application.domain.models.aggregate

import application.domain.models.Amount
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Tax
import application.domain.models.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ApprovedLoan")
data class ApprovedLoan(
    override val version: Version,
    override val identity: LoanId,
    val proposals: Proposals,
    val amount: Amount,
    val tax: Tax
) : Loan
