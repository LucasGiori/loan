package application.handlers

import application.commands.LoanRequestCommand
import application.ports.inbound.LoanRequestPort
import application.ports.outbound.LoanRepositoryPort
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class LoanRequestHandler(private val repository: LoanRepositoryPort) :
    Handler<LoanRequestCommand>,
    LoanRequestPort {
    override fun execute(command: LoanRequestCommand) {
        runBlocking {
            repository
                .pull(command.id)
                ?.request(command.proposalId)
                ?.let { event -> repository.push(event) }
        }
    }
}
