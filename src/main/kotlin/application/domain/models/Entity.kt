package application.domain.models

interface Entity {
    val identity: Identity

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}