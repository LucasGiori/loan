package driven.database.extension.models.aggregate

import application.domain.models.Customer
import application.domain.models.LoanId
import application.domain.models.UUIDv4
import application.domain.models.Version
import application.domain.models.aggregate.InitializedLoan
import driven.database.AggregateRecoveryFactory
import io.vertx.core.json.JsonObject
import kotlinx.serialization.json.Json

fun AggregateRecoveryFactory.Companion.initializedLoan(loan: JsonObject): InitializedLoan {
    val customer = loan.getString("customer").let { Json.decodeFromString<Customer>(it) }
    val loanId = LoanId(UUIDv4(loan.getString("id")))

    return InitializedLoan(
        customer = customer,
        version = Version(loan.getInteger("version")),
        identity = loanId
    )
}