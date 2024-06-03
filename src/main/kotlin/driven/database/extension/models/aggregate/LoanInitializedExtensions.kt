package driven.database.extension.models.aggregate

import application.domain.exceptions.UnknowLoanException
import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.UUIDv4
import application.domain.models.Version
import application.domain.models.aggregate.InitializedLoan
import driven.database.AggregateRecoveryFactory
import driven.database.LoanDto
import kotlinx.serialization.json.Json

fun AggregateRecoveryFactory.Companion.initializedLoan(loanDto: LoanDto): InitializedLoan {
    val customer = loanDto.customer?.let { Json.decodeFromString<Customer>(it) }
    val loanId = LoanId(UUIDv4(loanDto.loanId))

    return customer?.let {
        InitializedLoan(
            customer = customer,
            version = Version(loanDto.version),
            identity = loanId
        )
    } ?: throw UnknowLoanException(loanId = loanId)
}