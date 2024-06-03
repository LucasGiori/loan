package driven.database.extension.events

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.events.LoanProposalsIssuedEvent
import application.domain.events.LoanRequestedEvent
import application.domain.models.Status

val LoanEvent.status
    get() =
        when (this) {
            is LoanInitializedEvent -> Status.INITIALIZED
            is LoanProposalsIssuedEvent -> Status.AVAILABLE
            is LoanRequestedEvent -> Status.REQUESTED
            else -> throw IllegalArgumentException("Unknown LoanEvent type: ${this::class.java}")
        }
