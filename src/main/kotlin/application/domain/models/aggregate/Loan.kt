package application.domain.models.aggregate

import application.domain.events.LoanInitializedEvent
import application.domain.models.*

sealed interface Loan: AggregateRoot {
    override val identity: LoanId

    companion object {
        fun init(id: LoanId, customer: Customer) = LoanInitializedEvent(
            customer = customer,
            loanId = id,
            version = Version.INITIAL
        )
    }

    val status: Status
        get() = when (this) {
            is InitializedLoan -> Status.INITIALIZED
        }
}