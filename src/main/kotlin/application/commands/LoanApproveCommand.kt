package application.commands

import application.domain.models.LoanId

data class LoanApproveCommand(val id: LoanId) : Command
