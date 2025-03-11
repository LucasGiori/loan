package application.commands

import application.domain.models.LoanId

data class LoanProposalGeneratorCommand(val id: LoanId): Command