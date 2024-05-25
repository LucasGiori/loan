package application.domain.events

import application.domain.models.LoanId
import application.domain.models.Version

/** Porque usar sealed interface? */
sealed interface LoanEvent : DomainEvent {
    val version: Version
    val loanId: LoanId
}