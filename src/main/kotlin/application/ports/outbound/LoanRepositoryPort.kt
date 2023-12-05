package application.ports.outbound

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId

interface LoanRepositoryPort {
    /** @todo LoanEvent? */
    fun pull(loanId: LoanId): LoanEvent?

    fun push(event: LoanInitializedEvent)

    fun push(event: LoanEvent)
}