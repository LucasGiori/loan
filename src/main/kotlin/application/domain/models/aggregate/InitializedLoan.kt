package application.domain.models.aggregate

import application.domain.events.LoanRequestedEvent
import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.Version

data class InitializedLoan(
    val customer: Customer,
    override val version: Version,
    override val identity: LoanId
) : Loan {
    fun request() = LoanRequestedEvent(loanId = identity, version = version.next())
}