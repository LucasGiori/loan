package driver.http.request

import application.commands.LoanRequestCommand
import application.domain.models.LoanId
import application.domain.models.ProposalId
import application.domain.models.UUIDv4
import driver.Request

data class LoanRequest(val id: String, val proposalId: String): Request {
    override fun toCommand() = LoanRequestCommand(LoanId(UUIDv4(id)), ProposalId(UUIDv4(proposalId)))
}