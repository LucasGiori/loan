package application.domain.models.aggregate

import application.domain.events.LoanRequestedEvent
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ProposalsIssuedLoan")
data class ProposalsIssuedLoan(
    override val version: Version,
    override val identity: LoanId,
    val proposals: Proposals
) : Loan {
    override fun request(): LoanRequestedEvent {
        return LoanRequestedEvent(loanId = identity, version = version.next())
    }
}