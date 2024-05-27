package driven.database

import application.domain.models.Status
import application.domain.models.aggregate.Loan
import driven.database.extension.models.aggregate.initializedLoan

class AggregateRecoveryFactory {
    companion object {
        fun from(loanDto: LoanDto): Loan {
            val status = Status.valueOf(value = loanDto.status)
            return when(status) {
                Status.INITIALIZED -> initializedLoan(loanDto)
                Status.REQUESTED -> TODO()
                Status.ABANDONED -> TODO()
                Status.COMPLETED -> TODO()
                Status.CANCELED -> TODO()
            }
        }
    }
}