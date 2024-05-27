package application.handlers

import application.commands.LoanProposalGeneratorCommand
import application.ports.inbound.LoanProposalGeneratorPort
import application.ports.outbound.LoanRepositoryPort
import application.ports.outbound.LoggerPort
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class LoanProposalGeneratorHandler(private val repository: LoanRepositoryPort, private val logger: LoggerPort):
    Handler<LoanProposalGeneratorCommand>,
    LoanProposalGeneratorPort {
    override fun execute(command: LoanProposalGeneratorCommand) {
        runBlocking {
            repository
                .pull(command.id)
                ?.let {
                    logger.info(key="proposal_handler", data=it.toString())
                }
        }
    }
}