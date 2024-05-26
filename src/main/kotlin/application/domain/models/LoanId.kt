package application.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable
data class LoanId(
    @Serializable(with = UUIDv4Serializer::class)
    @Contextual
    val value: UUIDv4
) : Identity {
    override fun toString() = value.toString()
}

object UUIDv4Serializer : KSerializer<UUIDv4> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUIDv4", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUIDv4) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUIDv4 {
        return UUIDv4(UUID.fromString(decoder.decodeString()).toString())
    }
}