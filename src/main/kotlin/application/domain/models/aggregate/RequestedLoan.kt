package application.domain.models.aggregate

import application.domain.events.LoanApprovedEvent
import application.domain.events.LoanDeclinedEvent
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
) : Loan {
    override fun approve(): LoanApprovedEvent = LoanApprovedEvent(
        loanId = identity,
        version = version.next(),
        proposals = proposals
    )

    override fun decline(): LoanDeclinedEvent = LoanDeclinedEvent(
        loanId = identity,
        version = version.next(),
        proposals = proposals
    )
}
