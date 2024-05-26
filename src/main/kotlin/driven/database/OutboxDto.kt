package driven.database

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}