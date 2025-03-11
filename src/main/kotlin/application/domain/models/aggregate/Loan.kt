package application.domain.models.aggregate

import application.domain.events.LoanInitializedEvent
import application.domain.events.LoanProposalsIssuedEvent
import application.domain.events.LoanRequestedEvent
import application.domain.exceptions.IllegalStateTransitionException
import application.domain.models.*
import kotlinx.serialization.Serializable

@Serializable
sealed interface Loan: AggregateRoot {
    override val identity: LoanId

    companion object {
        fun init(id: LoanId, customer: Customer): LoanInitializedEvent = LoanInitializedEvent(
            customer = customer,
            loanId = id,
            version = Version.INITIAL
        )
    }

    val status: Status
        get() = when (this) {
            is InitializedLoan -> Status.INITIALIZED
            is ProposalsIssuedLoan -> Status.AVAILABLE
            is RequestedLoan -> Status.REQUESTED
            else -> throw RuntimeException("Event without mapped status")
        }

    fun issueProposals(): LoanProposalsIssuedEvent = throw IllegalStateTransitionException(status, Status.AVAILABLE)

    fun request(proposalId: ProposalId): LoanRequestedEvent = throw IllegalStateTransitionException(status, Status.REQUESTED)
}