package application.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val name: Name,
    val document: Document,
    val age: Age,
    val location: String,
    val profile: Profile,
    val income: Income
) : ValueObject