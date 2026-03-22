package driver.http.request

import application.commands.LoanRequestCommand
import application.domain.models.LoanId
import application.domain.models.UUIDv4
import driver.Request

data class LoanRequestRequest(val id: String) : Request {
    override fun toCommand() = LoanRequestCommand(
        LoanId(UUIDv4(id))
    )
}
