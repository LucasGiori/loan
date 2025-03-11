package application.domain.events

import application.domain.models.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LoanProposalsIssuedEvent")
data class LoanProposalsIssuedEvent(
    override val version: Version,
    override val loanId: LoanId,
    val proposals: Proposals
): LoanEvent