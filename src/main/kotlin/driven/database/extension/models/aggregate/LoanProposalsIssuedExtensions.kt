package driven.database.extension.models.aggregate

import application.domain.exceptions.UnknowLoanException
import application.domain.models.LoanId
import application.domain.models.Proposals
import application.domain.models.UUIDv4
import application.domain.models.Version
import application.domain.models.aggregate.ProposalsIssuedLoan
import driven.database.AggregateRecoveryFactory
import driven.database.LoanDto
import kotlinx.serialization.json.Json

fun AggregateRecoveryFactory.Companion.proposalsIssuedLoan(loanDto: LoanDto): ProposalsIssuedLoan {
    val loanId = LoanId(UUIDv4(loanDto.loanId))
    val proposals = loanDto.proposals
        ?.let { Json.decodeFromString<Proposals>(it) }
        ?: throw UnknowLoanException(loanId = loanId)

    return ProposalsIssuedLoan(
        version = Version(loanDto.version),
        identity = loanId,
        proposals = proposals
    )
}