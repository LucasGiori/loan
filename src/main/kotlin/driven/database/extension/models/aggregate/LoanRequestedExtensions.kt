package driven.database.extension.models.aggregate

import application.domain.exceptions.UnknowLoanException
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.UUIDv4
import application.domain.models.Version
import application.domain.models.aggregate.RequestedLoan
import driven.database.AggregateRecoveryFactory
import io.vertx.core.json.JsonObject
import kotlinx.serialization.json.Json


fun AggregateRecoveryFactory.Companion.requestedLoan(loan: JsonObject): RequestedLoan {
    val loanId = LoanId(UUIDv4(loan.getString("id")))

    val proposals = loan.getString("proposals")
        ?.let { Json.decodeFromString<Proposals>(it) }
        ?: throw UnknowLoanException(loanId = loanId)

    return RequestedLoan(
        version = Version(loan.getInteger("version")),
        identity = loanId,
        proposals = proposals
    )
}