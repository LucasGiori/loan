package application.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import starter.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Income(
    @Serializable(with = BigDecimalSerializer::class)
    @Contextual
    val value: BigDecimal
): ValueObject {
    init {
        require(value >= MIN_VALUE) { "Income less than $MIN_VALUE" }
    }

    companion object {
        private val MIN_VALUE = BigDecimal.valueOf(0.01)

        fun from(value: Double) = Income(BigDecimal.valueOf(value))
    }
}

