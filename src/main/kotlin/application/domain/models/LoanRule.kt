package application.domain.models

import java.math.BigDecimal

interface LoanRule {
    val loanType: LoanType

    fun calculateTax(income: Income): Tax {
        return Tax(BigDecimal(0))
    }

    fun calculateMaxLoanAmount(income: Income): Amount {
        return Amount(BigDecimal(1000))
    }

    fun getProposalValidity(): Int

    fun availableTo(): List<Profile>
}