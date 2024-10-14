package application.domain.models

import java.math.BigDecimal

class SecuredLoan : LoanRule {
    private companion object {
        const val MAX_LOAN_PERCENTAGE = 0.5
        const val PROPOSAL_VALIDITY_DAYS = 30
    }

    override val loanType: LoanType
        get() = LoanType.SECURED

    override fun calculateTax(income: Income): Tax {
        val taxRateValue = when {
            income.value <= BigDecimal(2000) -> BigDecimal(1.2)
            income.value <= BigDecimal(5000) -> BigDecimal(1.1)
            income.value <= BigDecimal(10000) -> BigDecimal(1.0)
            else -> BigDecimal(0.9)
        }

        return Tax(value=taxRateValue)
    }

    override fun calculateMaxLoanAmount(income: Income): Amount {
        val max = income.value.multiply(BigDecimal(MAX_LOAN_PERCENTAGE))

        return Amount(max)
    }

    override fun getProposalValidity(): Int {
        return PROPOSAL_VALIDITY_DAYS
    }

    override fun availableTo(): List<Profile> {
        return listOf(
            Profile.PENSIONER,
            Profile.RETIREE,
            Profile.EMPLOYEE,
            Profile.PUBLIC_SERVER,
            Profile.SELF_EMPLOYED
        )
    }
}