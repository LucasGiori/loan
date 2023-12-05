package application.domain.events

import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.Version

data class LoanInitializedEvent (
    val customer: Customer,
    override val loanId: LoanId,
    override val version: Version
) : LoanEvent