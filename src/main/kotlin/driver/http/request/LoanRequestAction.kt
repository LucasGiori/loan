package driver.http.request

import application.commands.LoanRequestCommand
import application.domain.models.LoanId
import application.domain.models.ProposalId
import application.domain.models.UUIDv4
import application.ports.inbound.LoanRequestPort
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.CREATED

@Path("/loans")
class LoanRequestAction(private val useCase: LoanRequestPort) {
    @POST
    @Path("/{id}/request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun request(@PathParam("id") id: String, body: LoanRequestRequest): Response {
        val command = LoanRequestCommand(
            id = LoanId(UUIDv4(id)),
            proposalId = ProposalId(UUIDv4(body.proposalId))
        )

        useCase.execute(command)

        return Response.status(CREATED).build()
    }
}
