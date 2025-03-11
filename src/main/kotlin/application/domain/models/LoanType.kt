package application.domain.models

enum class LoanType(val value: String) {
    PERSONAL("PERSONAL"),
    PAYROLL("PAYROLL"),
    SECURED("SECURED");

    companion object {
        fun from (value: String): LoanType {
            return entries
                .firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("${LoanType::class.java.simpleName} enum does not contains $value.")
        }
    }
}