package driver.http.init

import application.ports.inbound.LoanInitPort
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.CREATED

@Path("/loans")
class LoanInitAction(private val useCase: LoanInitPort) {
    @POST
    @Path("/initialize")
    @Produces(MediaType.APPLICATION_JSON)
    fun create(body: LoanInitRequest): Response {
        val command = body.toCommand()

        useCase.execute(command)

        return Response.status(CREATED).build()
    }
}