package application.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import starter.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Tax(
    @Serializable(with = BigDecimalSerializer::class)
    @Contextual
    val value: BigDecimal
): ValueObject {
    init {
        require(value >= MIN_VALUE) { "Tax less than $MIN_VALUE" }
    }

    companion object {
        private val MIN_VALUE = BigDecimal.valueOf(0.0)

        fun from(value: Double) = Tax(BigDecimal.valueOf(value))
    }
}