package driven.database

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import starter.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class LoanDto (
    val loanId: String,
    val customer: String? = null,
    val status: String,
    val version: Int,
    val proposals: String? = null,
    val type: String? = null,
//    val amount:  BigDecimal? = null
//    val tax: BigDecimal? = null
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual
    val createdAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual
    val updatedAt: LocalDateTime? = null
)

