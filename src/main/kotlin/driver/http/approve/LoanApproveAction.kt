package driver.http.approve

import application.commands.LoanApproveCommand
import application.domain.models.LoanId
import application.domain.models.UUIDv4
import application.ports.inbound.LoanApprovePort
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.CREATED

@Path("/loans")
class LoanApproveAction(private val useCase: LoanApprovePort) {
    @POST
    @Path("/{id}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    fun approve(@PathParam("id") id: String): Response {
        useCase.execute(LoanApproveCommand(LoanId(UUIDv4(id))))
        return Response.status(CREATED).build()
    }
}
