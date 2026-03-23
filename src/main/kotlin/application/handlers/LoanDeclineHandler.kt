package application.handlers

import application.commands.LoanDeclineCommand
import application.ports.inbound.LoanDeclinePort
import application.ports.outbound.LoanRepositoryPort
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class LoanDeclineHandler(private val repository: LoanRepositoryPort) :
    Handler<LoanDeclineCommand>,
    LoanDeclinePort {
    override fun execute(command: LoanDeclineCommand) {
        runBlocking {
            repository
                .pull(command.id)
                ?.decline()
                ?.let { event -> repository.push(event) }
        }
    }
}
