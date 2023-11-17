import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.OK

@Path("/hello")
class HelloResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(): Response {
        return Response.status(OK).entity(mapOf("message" to "Hello!!")).build()
    }
}