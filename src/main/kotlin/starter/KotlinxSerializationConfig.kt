package starter

import application.domain.events.LoanEvent
import application.domain.events.LoanInitializedEvent
import application.domain.events.LoanProposalsIssuedEvent
import application.domain.events.LoanRequestedEvent
import application.domain.models.aggregate.InitializedLoan
import application.domain.models.aggregate.Loan
import application.domain.models.aggregate.ProposalsIssuedLoan
import jakarta.ws.rs.ext.ContextResolver
import jakarta.ws.rs.ext.Provider
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Provider
class KotlinxSerializationConfig : ContextResolver<Json> {
    override fun getContext(type: Class<*>?): Json {
        return Json {
            prettyPrint = true
            isLenient = true
            serializersModule = SerializersModule {
                polymorphic(LoanEvent::class) {
                    subclass(LoanInitializedEvent::class)
                    subclass(LoanProposalsIssuedEvent::class)
                    subclass(LoanRequestedEvent::class)
                }
                polymorphic(Loan::class) {
                    subclass(InitializedLoan::class)
                    subclass(ProposalsIssuedLoan::class)
                }
            }
        }
    }
}