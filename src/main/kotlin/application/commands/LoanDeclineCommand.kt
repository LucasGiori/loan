package application.commands

import application.domain.models.LoanId

data class LoanDeclineCommand(val id: LoanId) : Command
