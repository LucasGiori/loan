package application.domain.events

import application.domain.models.LoanId
import application.domain.models.Version

data class LoanRequestedEvent(
    override val loanId: LoanId,
    override val version: Version
): LoanEvent