package application.commands

import application.domain.models.LoanId

data class LoanRequestCommand(val id: LoanId) : Command
