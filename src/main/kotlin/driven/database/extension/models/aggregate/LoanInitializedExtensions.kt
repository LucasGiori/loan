package driven.database.extension.models.aggregate

import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.UUIDv4
import application.domain.models.Version
import application.domain.models.aggregate.InitializedLoan
import driven.database.AggregateRecoveryFactory
import driven.database.LoanDto
import kotlinx.serialization.json.Json

fun AggregateRecoveryFactory.Companion.initializedLoan(loanDto: LoanDto): InitializedLoan {
    val customer = Json.decodeFromString<Customer>(loanDto.customer)

    return InitializedLoan(
        customer = customer,
        version = Version(loanDto.version),
        identity = LoanId(UUIDv4(loanDto.loanId))
    )
}