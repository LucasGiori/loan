package driven.database

import application.domain.models.Status
import application.domain.models.aggregate.Loan
import driven.database.extension.models.aggregate.initializedLoan
import driven.database.extension.models.aggregate.proposalsIssuedLoan
import io.vertx.core.json.JsonObject

class AggregateRecoveryFactory {
    companion object {
        fun from(loan: JsonObject): Loan {
            val status = Status.valueOf(value = loan.getString("status"))
            return when(status) {
                Status.INITIALIZED -> initializedLoan(loan)
                Status.AVAILABLE -> proposalsIssuedLoan(loan)
                Status.REQUESTED -> TODO()
                Status.ABANDONED -> TODO()
                Status.COMPLETED -> TODO()
                Status.CANCELED -> TODO()
            }
        }
    }
}