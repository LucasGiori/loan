package application.domain.events

import application.domain.models.*
import kotlinx.serialization.Serializable

@Serializable
data class LoanProposalsIssuedEvent(
    override val version: Version,
    override val loanId: LoanId,
    val proposals: Proposals
): LoanEvent