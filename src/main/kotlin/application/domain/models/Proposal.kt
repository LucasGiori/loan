package application.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import starter.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Proposal(
    val proposalId: ProposalId,
    val type: LoanType,
    val amount: Amount,
    val tax: Tax,
    val status: ProposalStatus,
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual
    val expiration: LocalDateTime
) : ValueObject