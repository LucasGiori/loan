package application.handlers

import application.commands.LoanApproveCommand
import application.ports.inbound.LoanApprovePort
import application.ports.outbound.LoanRepositoryPort
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class LoanApproveHandler(private val repository: LoanRepositoryPort) :
    Handler<LoanApproveCommand>,
    LoanApprovePort {
    override fun execute(command: LoanApproveCommand) {
        runBlocking {
            repository
                .pull(command.id)
                ?.approve()
                ?.let { event -> repository.push(event) }
        }
    }
}
