package application.domain.exceptions

import application.domain.models.Status

data class IllegalStateTransitionException(val from: Status, val to: Status) : DomainException()
