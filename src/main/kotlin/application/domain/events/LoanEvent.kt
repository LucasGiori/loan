package application.domain.events

import application.domain.models.LoanId
import application.domain.models.Version
import kotlinx.serialization.Serializable

@Serializable
sealed interface LoanEvent : DomainEvent {
    val version: Version
    val loanId: LoanId
}