package driven.database

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.models.LoanId
import application.ports.outbound.LoanRepositoryPort
import jakarta.inject.Singleton

@Singleton
class InMemoryAdapter : LoanRepositoryPort{
    /** @todo LoanEvent? */
    private val loans = mutableMapOf<String, LoanEvent>()

    override fun pull(loanId: LoanId): LoanEvent? {
        return loans[loanId.value.toString()];
    }

    override fun push(event: LoanInitializedEvent) {
        loans[event.loanId.value.toString()] = event
    }

    override fun push(event: LoanEvent) {
        loans[event.loanId.value.toString()] = event
    }
}