package application.domain.events

import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LoanDeclinedEvent")
data class LoanDeclinedEvent(
    override val loanId: LoanId,
    override val version: Version,
    val proposals: Proposals
) : LoanEvent
