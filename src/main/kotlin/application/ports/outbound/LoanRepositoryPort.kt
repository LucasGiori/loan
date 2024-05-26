package application.ports.outbound

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId

interface LoanRepositoryPort {
    /** @todo LoanEvent? */
    suspend fun pull(loanId: LoanId): LoanEvent?

    suspend fun push(event: LoanInitializedEvent)

    suspend fun push(event: LoanEvent)
}