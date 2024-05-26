package application.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@Serializable

data class Income(
    @Serializable(with = BigDecimalSerializer::class)
    @Contextual
    val value: BigDecimal
): ValueObject {
    init {
        require(value >= MIN_VALUE) { "Amount less than $MIN_VALUE" }
    }

    companion object {
        private val MIN_VALUE = BigDecimal.valueOf(0.01)

        fun from(value: Double) = Income(BigDecimal.valueOf(value))
    }
}

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString())
    }
}