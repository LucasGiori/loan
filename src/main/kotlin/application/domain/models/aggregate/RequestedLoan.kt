package application.domain.models.aggregate

import application.domain.events.LoanApprovedEvent
import application.domain.events.LoanDeclinedEvent
import application.domain.models.LoanId
import application.domain.models.ProposalStatus
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
    override fun approve(): LoanApprovedEvent {
        val accepted = proposals.getByStatus(ProposalStatus.ACCEPTED)
            ?: throw IllegalStateException("No accepted proposal found for loan $identity")

        return LoanApprovedEvent(
            loanId = identity,
            version = version.next(),
            proposals = proposals,
            amount = accepted.amount,
            tax = accepted.tax
        )
    }

    override fun decline(): LoanDeclinedEvent = LoanDeclinedEvent(
        loanId = identity,
        version = version.next(),
        proposals = proposals
    )
}
