package driver.http.loan

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.OK

@Path("/loan")
class LoanAction {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun loan(): Response {
        val response = """
            {
                "customer": {
                    "name": "Erikaya",
                    "document": "12345678910"
                },
                "loans": [
                    {
                        "type": "personal",
                        "value": 750,
                        "tax": 1.5,
                        "proposal": {
                            "id": "e53de8ae-3bdc-43d7-9ba0-cd2f4cbfaebe",
                            "valid": "2021-01-07"
                        }
                    },
                    {
                        "type": "guaranteed",
                        "value": 1500,
                        "tax": 1.2,
                        "proposal": {
                            "id": "14490692-4cde-4496-b6ee-11bfc5901b13",
                            "valid": "2021-01-30"
                        }
                    },
                    {
                        "type": "consignment",
                        "value": 900,
                        "tax": 1.4,
                        "proposal": {
                            "id": "fa52f689-adda-4883-88ea-81a0096e1b44",
                            "valid": "2021-01-30"
                        }
                    }
                ]
            }
        """.trimIndent()

        return Response.status(OK).entity(response).build()
    }
}