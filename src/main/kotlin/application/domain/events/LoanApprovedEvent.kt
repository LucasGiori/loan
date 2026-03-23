package application.domain.events

import application.domain.models.Amount
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Tax
import application.domain.models.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LoanApprovedEvent")
data class LoanApprovedEvent(
    override val loanId: LoanId,
    override val version: Version,
    val proposals: Proposals,
    val amount: Amount,
    val tax: Tax
) : LoanEvent
