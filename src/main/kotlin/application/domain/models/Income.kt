package application.domain.models

import java.math.BigDecimal

data class Income(val value: BigDecimal): ValueObject {
    init {
        require(value >= MIN_VALUE) { "Amount less than $MIN_VALUE" }
    }

    companion object {
        private val MIN_VALUE = BigDecimal.valueOf(0.01)

        fun from(value: Double) = Income(BigDecimal.valueOf(value))
    }
}