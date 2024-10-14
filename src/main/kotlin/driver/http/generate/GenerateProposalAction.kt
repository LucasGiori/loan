package driver.http.generate

import application.ports.inbound.LoanProposalGeneratorPort
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.CREATED

@Path("/loans")
class GenerateProposalAction(private val useCase: LoanProposalGeneratorPort) {
    @POST
    @Path("/proposals/generate")
    @Produces(MediaType.APPLICATION_JSON)
    fun create(body: GenerateProposalRequest): Response {
        val command = body.toCommand()

        useCase.execute(command)

        return Response.status(CREATED).build()
    }
}