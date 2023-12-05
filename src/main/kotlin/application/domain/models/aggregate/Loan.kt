package application.domain.models.aggregate

import application.domain.events.LoanInitializedEvent
import application.domain.models.AggregateRoot
import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.Version

sealed interface Loan: AggregateRoot {
    override val identity: LoanId

    companion object {
        fun init(id: LoanId, customer: Customer) = LoanInitializedEvent(
            customer = customer,
            loanId = id,
            version = Version.INITIAL
        )
    }
}