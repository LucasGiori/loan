package driver.http.request

import application.ports.inbound.LoanRequestPort
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
    @Produces(MediaType.APPLICATION_JSON)
    fun request(@PathParam("id") id: String): Response {
        val command = LoanRequestRequest(id).toCommand()

        useCase.execute(command)

        return Response.status(CREATED).build()
    }
}
