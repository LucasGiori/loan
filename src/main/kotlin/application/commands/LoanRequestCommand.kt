package application.commands

import application.domain.models.LoanId
import application.domain.models.ProposalId

data class LoanRequestCommand(val id: LoanId, val proposalId: ProposalId): Command