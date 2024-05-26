package query.loan.available

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.NOT_FOUND
import jakarta.ws.rs.core.Response.Status.OK
import kotlinx.coroutines.runBlocking

@Path("/loans")
class GetLoanAvailableAction(private val handler: GetLoanAvailableHandler) {
    @GET
    @Path("/{loanId}/available")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLoanAvailable(@PathParam("loanId") loanId: String): Response {
        return runBlocking {
            handler
                .execute(GetLoanAvailableQuery(loanId))
                ?.let { Response.status(OK).entity(it).build() } ?: Response.status(NOT_FOUND).build()
        }
    }
}