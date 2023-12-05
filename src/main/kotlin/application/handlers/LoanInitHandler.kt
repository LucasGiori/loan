package application.handlers

import application.commands.LoanInitCommand
import application.domain.models.aggregate.Loan
import application.ports.inbound.LoanInitPort
import application.ports.outbound.LoanRepositoryPort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
data class LoanInitHandler(private val repository: LoanRepositoryPort) :
    Handler<LoanInitCommand>,
    LoanInitPort {
    override fun execute(command: LoanInitCommand) {
        Loan
            .init(command.id, command.customer)
            .also { event ->
                repository.push(event)
            }
    }

}