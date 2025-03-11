package application.commands

import application.domain.models.Customer
import application.domain.models.LoanId

data class LoanInitCommand(val id: LoanId, val customer: Customer): Command