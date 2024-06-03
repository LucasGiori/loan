package driven.database

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import starter.serializer.InstantSerializer
import java.time.Instant
import java.util.*

@Serializable
data class OutboxDto(
    val revision: Int = 1,
    @Serializable(with = InstantSerializer::class)
    @Contextual
    val datOccurred: Instant = Instant.now(),
    val codOutboxEvent: String = UUID.randomUUID().toString(),
    val desAggregateType: String = "Loan",
    val type: String?,
    val payload: String?, //json
    val identity: String?,
    val snapshot: String?, //json
)

