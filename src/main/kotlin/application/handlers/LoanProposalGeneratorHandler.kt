package application.handlers

import application.commands.LoanProposalGeneratorCommand
import application.domain.models.*
import application.ports.inbound.LoanProposalGeneratorPort
import application.ports.outbound.LoanRepositoryPort
import application.ports.outbound.LoggerPort
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@ApplicationScoped
class LoanProposalGeneratorHandler(private val repository: LoanRepositoryPort, private val logger: LoggerPort):
    Handler<LoanProposalGeneratorCommand>,
    LoanProposalGeneratorPort {
    override fun execute(command: LoanProposalGeneratorCommand) {
        runBlocking {
            repository
                .pull(command.id)
                ?.issueProposals(
                    Proposals(
                        proposals = mutableListOf(
                            Proposal(
                                proposalId = ProposalId(UUIDv4(UUID.randomUUID().toString())),
                                type = LoanType.PAYROLL,
                                amount = Amount(BigDecimal(10.90)),
                                tax = Tax(BigDecimal(1.5)),
                                status = ProposalStatus.SUGGESTED,
                                expiration = LocalDateTime.now()
                            )
                        )
                    )
                )
                ?.let { event -> repository.push(event) }
        }
    }
}