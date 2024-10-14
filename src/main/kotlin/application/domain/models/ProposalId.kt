package application.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import starter.serializer.UUIDv4Serializer

@Serializable
data class ProposalId(
    @Serializable(with = UUIDv4Serializer::class)
    @Contextual
    val value: UUIDv4
) : Identity {
    override fun toString() = value.toString()
}