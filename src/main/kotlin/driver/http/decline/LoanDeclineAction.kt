package driver.http.decline

import application.commands.LoanDeclineCommand
import application.domain.models.LoanId
import application.domain.models.UUIDv4
import application.ports.inbound.LoanDeclinePort
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.CREATED

@Path("/loans")
class LoanDeclineAction(private val useCase: LoanDeclinePort) {
    @POST
    @Path("/{id}/decline")
    @Produces(MediaType.APPLICATION_JSON)
    fun decline(@PathParam("id") id: String): Response {
        useCase.execute(LoanDeclineCommand(LoanId(UUIDv4(id))))
        return Response.status(CREATED).build()
    }
}
