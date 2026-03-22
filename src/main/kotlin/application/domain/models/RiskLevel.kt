package application.domain.models

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH;

    companion object {
        fun from (loanType: LoanType): RiskLevel {
            return when(loanType) {
                LoanType.PERSONAL -> HIGH
                LoanType.PAYROLL -> MEDIUM
                LoanType.SECURED -> LOW
            }
        }
    }
}