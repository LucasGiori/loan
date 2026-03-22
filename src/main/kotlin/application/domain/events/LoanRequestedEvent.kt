package application.domain.events

import application.domain.models.LoanId
import application.domain.models.ProposalId
import application.domain.models.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LoanRequestedEvent")
data class LoanRequestedEvent(
    override val loanId: LoanId,
    override val version: Version,
    val proposalId: ProposalId
): LoanEvent