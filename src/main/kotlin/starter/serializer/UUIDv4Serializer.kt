package starter.serializer

import application.domain.models.UUIDv4
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object UUIDv4Serializer : KSerializer<UUIDv4> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUIDv4", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUIDv4) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUIDv4 {
        return UUIDv4(UUID.fromString(decoder.decodeString()).toString())
    }
}