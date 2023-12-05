package application.domain.models

data class LoanId(val value: UUIDv4) : Identity {
    override fun toString() = value.toString()
}