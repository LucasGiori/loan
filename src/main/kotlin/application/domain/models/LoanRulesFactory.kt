package application.domain.models

class LoanRulesFactory {
    companion object {
        fun get(): Map<LoanType, LoanRule> {
            return mapOf(
                LoanType.PAYROLL to PayrollLoan(),
                LoanType.SECURED to SecuredLoan(),
                LoanType.PERSONAL to PersonalLoan()
            )
        }
    }
}