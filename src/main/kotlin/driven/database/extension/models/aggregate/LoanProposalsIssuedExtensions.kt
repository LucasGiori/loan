package driven.database.extension.models.aggregate

import application.domain.exceptions.UnknowLoanException
import application.domain.models.*
import application.domain.models.aggregate.ProposalsIssuedLoan
import driven.database.AggregateRecoveryFactory
import io.vertx.core.json.JsonObject
import kotlinx.serialization.json.Json

fun AggregateRecoveryFactory.Companion.proposalsIssuedLoan(loan: JsonObject): ProposalsIssuedLoan {
    val loanId = LoanId(UUIDv4(loan.getString("id")))

    val proposals = loan.getString("proposals")
        ?.let { Json.decodeFromString<Proposals>(it) }
        ?: throw UnknowLoanException(loanId = loanId)

    return ProposalsIssuedLoan(
        version = Version(loan.getInteger("version")),
        identity = loanId,
        proposals = proposals
    )
}