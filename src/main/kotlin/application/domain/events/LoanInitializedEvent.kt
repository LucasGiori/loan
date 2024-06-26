package application.domain.events

import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.Version
import kotlinx.serialization.Serializable

@Serializable
data class LoanInitializedEvent (
    val customer: Customer,
    override val loanId: LoanId,
    override val version: Version
) : LoanEvent