package application.domain.events

import application.domain.models.LoanId
import application.domain.models.Version
import kotlinx.serialization.Serializable

@Serializable
data class LoanRequestedEvent(
    override val loanId: LoanId,
    override val version: Version
): LoanEvent