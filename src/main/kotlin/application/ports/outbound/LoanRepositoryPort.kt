package application.ports.outbound

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId
import application.domain.models.aggregate.Loan

interface LoanRepositoryPort {
    suspend fun pull(loanId: LoanId): Loan?

    suspend fun push(event: LoanInitializedEvent)

    suspend fun push(event: LoanEvent)
}