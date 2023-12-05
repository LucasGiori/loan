package application.domain.exceptions

import application.domain.models.LoanId

data class UnknowLoanException(val loanId: LoanId? = null) : DomainException()