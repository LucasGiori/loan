package application.domain.models.aggregate

import application.domain.events.LoanProposalsIssuedEvent
import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Version

data class InitializedLoan(
    val customer: Customer,
    override val version: Version,
    override val identity: LoanId
) : Loan {
    override fun issueProposals(proposals: Proposals): LoanProposalsIssuedEvent {
        return LoanProposalsIssuedEvent(
            loanId = identity,
            version = version.next(),
            proposals = proposals.copy()
        )
    }
}