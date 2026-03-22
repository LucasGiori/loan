package application.handlers

import application.commands.LoanProposalGeneratorCommand
import application.ports.inbound.LoanProposalGeneratorPort
import application.ports.outbound.LoanRepositoryPort
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class LoanProposalGeneratorHandler(private val repository: LoanRepositoryPort):
    Handler<LoanProposalGeneratorCommand>,
    LoanProposalGeneratorPort {
    override fun execute(command: LoanProposalGeneratorCommand) {
        runBlocking {
            repository
                .pull(command.id)
                ?.issueProposals()
                ?.let { event -> repository.push(event) }
        }
    }
}