package application.domain.models

sealed interface ValueObject {
    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}