package application.domain.models

interface AggregateRoot: Entity {
    val version: Version
}