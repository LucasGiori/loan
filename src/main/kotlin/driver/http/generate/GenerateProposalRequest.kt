package driver.http.generate

import application.commands.LoanProposalGeneratorCommand
import application.domain.models.*
import driver.Request

data class GenerateProposalRequest(val id: String): Request {
    override fun toCommand() = LoanProposalGeneratorCommand(
        LoanId(UUIDv4(id)),
    )
}